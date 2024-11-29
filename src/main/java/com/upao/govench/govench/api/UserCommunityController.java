package com.upao.govench.govench.api;

import com.upao.govench.govench.mapper.CommunityMapper;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.dto.UserCommunityResponseDTO;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.upao.govench.govench.model.entity.UserCommunity;
import com.upao.govench.govench.model.entity.IdCompuestoU_C;
import com.upao.govench.govench.service.UserCommunityService;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;



@AllArgsConstructor
@RestController


@RequestMapping("/admin/usercommunity")
public class UserCommunityController {
    @Autowired
    private UserCommunityService userCommunityService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityMapper communityMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserCommunity> getAllUserCommunities() {
        return userCommunityService.getAllUserCommunities();
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{iduser}/{idcommunity}")
    public UserCommunity getUserCommunityById(@PathVariable int iduser,@PathVariable int idcommunity) {
        IdCompuestoU_C id =new IdCompuestoU_C(iduser, idcommunity);
        return userCommunityService.searchUserCommunityById(id);
    }

    @GetMapping("/pertains")
    public List<UserCommunityResponseDTO> getComunitiesPertains() {
        Integer idUser = userService.getAuthenticatedUserIdFromJWT();
        User user = userService.getUserbyId(idUser);
        List<UserCommunity> userCommunities = userCommunityService.getUserCommunityByUser(user);
        return communityMapper.convertToListUserCommunityResponseDTO(userCommunities);
    }

    @PostMapping("/{idcommunity}")
    @PreAuthorize("hasAnyRole('PARTICIPANT', 'ORGANIZER')")
    public ResponseEntity<String> createUserCommunity(@PathVariable int idcommunity) {
        // Obtener el ID del usuario autenticado desde el token JWT
        Integer iduser = userService.getAuthenticatedUserIdFromJWT();
        if (iduser == null) {
            return new ResponseEntity<>("Usuario no autenticado", HttpStatus.UNAUTHORIZED);
        }

        // Consultar el usuario por su ID
        User user = userService.getUserbyId(iduser);
        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        // Verificar si el rol del usuario es "ROLE_PARTICIPANT" o "ROLE_ORGANIZER"
        String userRole = user.getRole().getName();
        if ( user.getOrganizer() == null && user.getParticipant() == null ) {
            return new ResponseEntity<>("Solo los participantes y organizadores pueden unirse a una comunidad", HttpStatus.FORBIDDEN);
        }

        // Consultar la comunidad por su ID
        Community community = communityService.EntityfindById(idcommunity);
        if (community == null) {
            return new ResponseEntity<>("Comunidad no encontrada", HttpStatus.NOT_FOUND);
        }

        // Verificar si la relación ya existe
        if (userCommunityService.searchUserCommunityById(new IdCompuestoU_C(iduser, idcommunity)) != null) {
            return new ResponseEntity<>("Ya estás registrado en esta comunidad", HttpStatus.CONFLICT);
        }

        // Crear la nueva relación entre usuario y comunidad
        UserCommunity createdUserCommunity = userCommunityService.addUserCommunity(
                new UserCommunity(new IdCompuestoU_C(iduser, idcommunity), user, community, LocalDate.now())
        );

        return new ResponseEntity<>("Inscripción en la comunidad exitosa", HttpStatus.CREATED);
    }


    @DeleteMapping("/{iduser}/{idcommunity}")
    public ResponseEntity<String> deleteUserCommunity(@PathVariable int iduser, @PathVariable int idcommunity) {
        IdCompuestoU_C id = new IdCompuestoU_C(iduser, idcommunity);

        // Verificar si la relación existe antes de intentar eliminarla
        if (userCommunityService.searchUserCommunityById(id) != null) {
            userCommunityService.removeUserCommunityById(id);
            return new ResponseEntity<>("Se ha desasociado de la comunidad", HttpStatus.OK);
        } else {
            // Devolver un mensaje indicando que la relación no existe
            return new ResponseEntity<>("La relación ya no existe", HttpStatus.NOT_FOUND);
        }
    }
}

