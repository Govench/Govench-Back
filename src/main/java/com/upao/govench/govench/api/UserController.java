package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.exceptions.UserNotFoundException;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.security.UserPrincipal;
import com.upao.govench.govench.service.ProfileService;
import com.upao.govench.govench.service.UserService;
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

    @PostMapping("/upload/profile")
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


    @GetMapping("/profile/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable @Min(1) int userId) {
        Profile profile = null;

        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Usuario con ID " + userId + " no encontrado.")
        );

        if(user.getParticipant()!=null) {
            String profileId=user.getParticipant().getProfileId();
            if (profileId != null) {
                profile = profileService.getProfile(profileId);
            } else {
                throw new UserNotFoundException("El participante no tiene foto asociada.");
            }
        }
        if(user.getOrganizer()!=null) {
            String profileId=user.getOrganizer().getProfileId();
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

    @DeleteMapping("/delete/profile")
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
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al obtener la lista de usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PostMapping("/{userId}/rate/{ratedUserId}")
    public ResponseEntity<String> rateUser(
            @PathVariable Integer userId,
            @PathVariable Integer ratedUserId,
            @RequestBody Rating rating
    ) {
        try {
            userService.rateUser(userId, ratedUserId, rating.getRatingValue(), rating.getComment());

            return new ResponseEntity<>("Usuario calificado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al calificar usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/ratings")
    public ResponseEntity<List<RatingRequestDTO>> getUserRatings(@PathVariable Integer userId) {
        try {
            // Obtener las calificaciones
            List<Rating> ratings = userService.getUserRatings(userId);

            // Mapear a DTO
            List<RatingRequestDTO> ratingsDTO = ratings.stream().map(rating -> {
                String raterName = "";

                // Verificar quién otorgó la calificación (organizador o participante)
                if (rating.getRaterOrganizer() != null) {
                    // Si es un organizador que califica
                    raterName = rating.getRaterOrganizer().getName(); // Asegúrate de que 'getName()' esté implementado en Organizer
                } else if (rating.getRaterParticipant() != null) {
                    // Si es un participante que califica
                    raterName = rating.getRaterParticipant().getName(); // Asegúrate de que 'getName()' esté implementado en Participant
                }

                return new RatingRequestDTO(raterName, rating.getRatingValue(), rating.getComment());
            }).collect(Collectors.toList());

            return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/ratingEvent/{eventId}")
    public ResponseEntity<String> rateEvent(@PathVariable int eventId,
                                            @RequestBody RatingEventRequestDTO ratingEventRequestDTO) {

        Integer authenticatedUserId = userService.getAuthenticatedUserIdFromJWT();

        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));

        try{
            userService.createRatingEvent(user, event, ratingEventRequestDTO);
            return new ResponseEntity<>("Evento calificado correctamente", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Error al calificar evento", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/followers")
    public ResponseEntity<Integer> getFollowers() {
        int followers = userService.getFollowersCount();
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity<Integer> getFollowing() {
        int following = userService.getFollowingCount();
        return new ResponseEntity<>(following, HttpStatus.OK);
    }
}