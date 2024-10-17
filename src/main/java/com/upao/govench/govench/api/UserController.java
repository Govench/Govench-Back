package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.service.ProfileService;
import com.upao.govench.govench.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register/participant")
    public ResponseEntity<UserProfileDTO> registerParticipant(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserProfileDTO userProfileDTO = userService.registerParticipant(userRegistrationDTO);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.CREATED);
    }

    @PostMapping("/register/organizer")
    public ResponseEntity<UserProfileDTO> registerOrganizer(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserProfileDTO userProfileDTO = userService.registerOrganizer(userRegistrationDTO);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.CREATED);
    }

//Metodos pre security//
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al obtener la lista de usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Integer id, @RequestBody UserRequestDTO userRequestDTO) {
        try {
            userService.updateUser(id, userRequestDTO);
            return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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


    @PostMapping("/profile/{userId}/upload")
    public String uploadProfileImage(@PathVariable int userId, @RequestParam("file") MultipartFile file) {
        try {

            Profile profile = profileService.saveProfileWithImage(file);

            userService.associateProfileWithUser(userId, profile.getId());

            return "La imagen se ha asociado al perfil correctamente";

        } catch (IOException e) {
            return "Falla en la subida de la imagen";
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable int userId) {
        User user = userService.getUserbyId(userId);
        String profileId=user.getParticipant().getProfileId();
        Profile profile = profileService.getProfile(profileId);
        if (profile != null && profile.getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.IMAGE_JPEG) // o el tipo de imagen adecuado
                    .body(profile.getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("profile/{userId}")
    public ResponseEntity<String> deleteProfileImage(@PathVariable int userId) {
        User user = userService.getUserbyId(userId);
        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        String profileId = user.getParticipant().getProfileId();
        if (profileId == null) {
            return new ResponseEntity<>("Usuario no tiene una foto de perfil asociada", HttpStatus.NOT_FOUND);
        }
        Profile profile = profileService.getProfile(profileId);
        if (profile == null || profile.getImage() == null) {
            return new ResponseEntity<>("Foto de perfil no encontrada", HttpStatus.NOT_FOUND);
        }
        userService.dessasociateProfileWithUser(userId);
        profileService.deleteProfile(profileId);
        return new ResponseEntity<>("Foto de perfil eliminada", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}/follow/{followedUserId}")
    public ResponseEntity<String> followUser(@PathVariable Integer userId, @PathVariable Integer followedUserId) {
        try {
            userService.followUser(userId, followedUserId);
            return new ResponseEntity<>("Usuario seguido correctamente", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Error al seguir al usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/unfollow/{removedUserId}")
    public ResponseEntity<String> removeFollowUser(@PathVariable Integer userId, @PathVariable Integer removedUserId) {
        try {
            userService.removeFollowUser(userId, removedUserId);
            return new ResponseEntity<>("Dejado de seguir Usuario correctamente", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Error al dejar de seguir al usuario", HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PostMapping("/{eventId}/ratingEvent/{userId}")
    public ResponseEntity<String> rateEvent(@PathVariable Integer eventId,
                                            @PathVariable Integer userId,
                                            @RequestBody RatingEventRequestDTO ratingEventRequestDTO) {

        try{
            userService.createRatingEvent(userId, eventId, ratingEventRequestDTO);
            return new ResponseEntity<>("Evento calificado correctamente", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Error al calificar evento", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
