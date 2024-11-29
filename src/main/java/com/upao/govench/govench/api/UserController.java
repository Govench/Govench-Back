package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.exceptions.UserNotFoundException;
import com.upao.govench.govench.mapper.RatingMapper;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.RatingEventRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.security.UserPrincipal;
import com.upao.govench.govench.service.ProfileService;
import com.upao.govench.govench.service.UserService;
import com.upao.govench.govench.security.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.TableOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final ProfileService profileService;

    private  final UserRepository userRepository;


    private final EventRepository eventRepository;

    private final RatingMapper ratingMapper;

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RatingEventRepository ratingEventRepository;

    @PostMapping("/upload/profile-photo")
    public ResponseEntity<String> uploadProfileImage( @RequestParam("file") MultipartFile file) {
        Integer authenticatedUserId = userService.getAuthenticatedUserIdFromJWT();
        User user = userRepository.findById(authenticatedUserId).orElseThrow(ResourceNotFoundException::new);
        if(user.getRole().getName().equals("ROLE_ADMIN"))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El admin no puede tener foto de perfil");
        }

        try {
            Profile profile = profileService.saveProfileWithImage(file);
            userService.associateProfileWithUser(authenticatedUserId, profile.getId());
            return new ResponseEntity<>("La imagen se ha asociado al perfil correctamente", HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("Falla en la subida de la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/profile-photo/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable @Min(1) int userId) {
        Profile profile = null;

        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Usuario con ID " + userId + " no encontrado.")
        );

        if (user.getParticipant() != null) {
            String profileId = user.getParticipant().getProfileId();
            if (profileId != null) {
                profile = profileService.getProfile(profileId);
            } else {
                throw new UserNotFoundException("El participante no tiene foto asociada.");
            }
        }
        if (user.getOrganizer() != null) {
            String profileId = user.getOrganizer().getProfileId();
            if (profileId != null) {
                profile = profileService.getProfile(profileId);
            } else {
                throw new UserNotFoundException("El participante no tiene foto asociada.");
            }
        }

        if (profile != null && profile.getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.IMAGE_JPEG) // o el tipo de imagen adecuado
                    .body(profile.getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/profile-photo/delete")
    public ResponseEntity<String> deleteProfileImage() {

        Integer authenticatedUserId = userService.getAuthenticatedUserIdFromJWT();



        User user = userService.getUserbyId(authenticatedUserId);
        String profileId=null;

        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        if(user.getAdmin()!=null)
        {
            return new ResponseEntity<>("Usuario admin no puede tener foto de perfil", HttpStatus.BAD_REQUEST);
        }
        if(user.getParticipant()!=null)
        {
            profileId = user.getParticipant().getProfileId();
        }
        if(user.getOrganizer()!=null)
        {
            profileId = user.getOrganizer().getProfileId();
        }
        if (profileId == null) {
            return new ResponseEntity<>("Usuario no tiene una foto de perfil asociada", HttpStatus.NOT_FOUND);
        }

        Profile profile = profileService.getProfile(profileId);

        if (profile == null || profile.getImage() == null) {
            return new ResponseEntity<>("Foto de perfil no encontrada", HttpStatus.NOT_FOUND);
        }

        userService.dessasociateProfileWithUser(authenticatedUserId);
        profileService.deleteProfile(profileId);
        return new ResponseEntity<>("Foto de perfil eliminada", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/desubscribe")
    public ResponseEntity<?> Desubscriber()
    {   userService.DesubscribePremium();
        return new ResponseEntity<>("Suscripcion anulada", HttpStatus.OK);
    }

    //-------Metodos pre security----------//
    @GetMapping("/all")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        try {
            List<UserProfileDTO> users = userService.getAllUsers(); // Obtén los usuarios convertidos a UserProfileDTO
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Integer getAuthenticatedUserIdFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getCredentials(); // Obtén el token del objeto de autenticación

            // Extraer el email del token
            Claims claims = tokenProvider.getJwtParser().parseClaimsJws(token).getBody();
            String email = claims.getSubject();

            // Buscar el usuario usando el email
            User user = userRepository.findByEmail(email).orElse(null);
            return user != null ? user.getId() : null;
        }
        return null; // Si no hay autenticación, devuelve null
    }

    @GetMapping("/ratings/{userId}")
    public ResponseEntity<List<RatingResponseDTO>> getRatingByID(@PathVariable Integer userId) {
        try {
            // Obtener las calificaciones que ha hecho el usuario
            List<Rating> ratings = userService.getUserRatings(userId);

            // Convertir las calificaciones a RatingResponseDTO
            List<RatingResponseDTO> ratingsDTO = ratingMapper.toRatingResponseDTOList(ratings);

            // Verificar si la lista está vacía
            if (ratingsDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/myratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings() {
        try {
            Integer userId = userService.getAuthenticatedUserIdFromJWT();
            // Obtener las calificaciones que ha hecho el usuario
            List<Rating> ratings = userService.getUserRatings(userId);

            // Convertir las calificaciones a RatingResponseDTO
            List<RatingResponseDTO> ratingsDTO = ratingMapper.toRatingResponseDTOList(ratings);

            // Verificar si la lista está vacía
            if (ratingsDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/myrateds")
    public ResponseEntity<List<RatingResponseDTO>> getRateds()
    {
        try {
            Integer userId = userService.getAuthenticatedUserIdFromJWT();
            // Obtener las calificaciones que le han hecho al usuario
            List<Rating> ratings = userService.getUserRated(userId);

            // Convertir las calificaciones a RatingResponseDTO
            List<RatingResponseDTO> ratingsDTO = ratingMapper.toRatedResponseDTOList(ratings);

            // Verificar si la lista está vacía
            if (ratingsDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rated/{userId}")
    public ResponseEntity<List<RatingResponseDTO>> getRatedsById(@PathVariable Integer userId)
    {
        try {
            // Obtener las calificaciones que le han hecho al usuario
            List<Rating> ratings = userService.getUserRated(userId);

            // Convertir las calificaciones a RatingResponseDTO
            List<RatingResponseDTO> ratingsDTO = ratingMapper.toRatedResponseDTOList(ratings);

            // Verificar si la lista está vacía
            if (ratingsDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rate/{ratedUserId}")
    public ResponseEntity<String> rateUser(@Valid
            @PathVariable Integer ratedUserId,
            @RequestBody RatingRequestDTO rating)
    {
            // Obtener el ID del usuario autenticado desde el token JWT
            Integer userId = getAuthenticatedUserIdFromJWT();

            // Verificar que el usuario esté autenticado
            if (userId == null) {
                return new ResponseEntity<>("Acceso denegado: Usuario no autenticado", HttpStatus.FORBIDDEN);
            }

            // Usar el servicio para calificar al usuario
            userService.rateUser(userId, ratedUserId, rating.getRatingValue(), rating.getComment());

            return new ResponseEntity<>("Usuario calificado correctamente", HttpStatus.OK);

    }

    @PostMapping("/ratingEvent/{eventId}")
    public ResponseEntity<String> rateEvent(@PathVariable int eventId,
                                            @RequestBody RatingEventRequestDTO ratingEventRequestDTO) {

        Integer authenticatedUserId = userService.getAuthenticatedUserIdFromJWT();

        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));

            userService.createRatingEvent(user, event, ratingEventRequestDTO);
            return new ResponseEntity<>("Evento calificado correctamente", HttpStatus.OK);

    }

    @GetMapping("/verification/{idEvent}")
    public boolean verification(@PathVariable int idEvent){

        Integer authenticatedUserId = userService.getAuthenticatedUserIdFromJWT();
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
        boolean verification = ratingEventRepository.existsByUserIdAndEventId(user,event);
        return verification;

    }


    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam Integer userId){
        userService.followUser(userId);
        return new ResponseEntity<>("Usuario seguido con éxito", HttpStatus.ACCEPTED);
    }

    @PutMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam Integer userId){
        userService.unfollowUser(userId);
        return new ResponseEntity<>("Has dejado de seguir al usuario", HttpStatus.OK);
    }

    @GetMapping("/quantity-followers")
    public ResponseEntity<Integer> getFollowers() {
        int followers = userService.getFollowersCount();
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @GetMapping("/quantity-following")
    public ResponseEntity<Integer> getFollowing() {
        int following = userService.getFollowingCount();
        return new ResponseEntity<>(following, HttpStatus.OK);
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowersDetails()
    {
       List<FollowResponseDTO> followers=  userService.getFollowers();
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
    @GetMapping("/followings")
    public ResponseEntity<?> getFollowingsDetails()
    {
        List<FollowResponseDTO> followers=  userService.getFollowings();
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @PutMapping("/edit-password")
    public ResponseEntity<?> editPassword(@Valid @RequestBody PasswordDTO passwordDTO){
        try {
            userService.updatePassword(passwordDTO);
            return ResponseEntity.ok().body("Contraseña actualizada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}