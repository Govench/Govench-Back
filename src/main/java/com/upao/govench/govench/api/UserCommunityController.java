package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.UserCommunityAlreadyExistsException;
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



    @PostMapping("/{iduser}/{idcommunity}")
    public ResponseEntity<?> createUserCommunity(@PathVariable int iduser, @PathVariable int idcommunity) {
        // Consultar el usuario por su ID
        User user = userService.getUserbyId(iduser);
        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND); // Mensaje más claro
        }

        // Consultar la comunidad por su ID
        Community community = communityService.EntityfindById(idcommunity);
        if (community == null) {
            return new ResponseEntity<>("Comunidad no encontrada", HttpStatus.NOT_FOUND); // Mensaje más claro
        }

        // Verificar si la relación ya existe
        if (userCommunityService.searchUserCommunityById(new IdCompuestoU_C(iduser, idcommunity)) != null) {
            throw new UserCommunityAlreadyExistsException("La relación ya existe entre el usuario y la comunidad."); // Lanzar excepción
        }

        // Crear la nueva relación entre usuario y comunidad
        UserCommunity createdUserCommunity = userCommunityService.addUserCommunity(
                new UserCommunity(new IdCompuestoU_C(iduser, idcommunity), user, community, LocalDate.now())
        );

        return new ResponseEntity<>(createdUserCommunity, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{iduser}/{idcommunity}")
    public void deleteUserCommunity(@PathVariable int iduser,@PathVariable int idcommunity) {
        IdCompuestoU_C id =new IdCompuestoU_C(iduser, idcommunity);
        userCommunityService.removeUserCommunityById(id);
    }
}

