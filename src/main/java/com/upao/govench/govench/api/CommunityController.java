package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/community")
@RestController

public class CommunityController {
    @Autowired
    private CommunityService communityService; // Inyección del servicio de Community
    @Autowired
    private UserService userService;


    @GetMapping("/communities")
    public List<CommunityResponseDTO> getCommunitiesNotCreatedByUser() throws Exception {

        return communityService.getall();
    }
    @GetMapping("/search/{id}")
    public CommunityResponseDTO getCommunityById(@PathVariable("id") int id)  {
        return  communityService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','PARTICIPANT')")
    @GetMapping("/my-communities")
    public List<CommunityResponseDTO> getCommunitiesByUser() throws Exception {
        Integer userid= userService.getAuthenticatedUserIdFromJWT();
        return communityService.findByOwner_Id(userid);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','PARTICIPANT')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable("id") int id) throws Exception {
        Integer userid= userService.getAuthenticatedUserIdFromJWT();
        User owner= userService.getUserbyId(userid);
        CommunityResponseDTO existingCommunity = communityService.findById(id);
        if (existingCommunity == null) {
            throw new NullPointerException("Esta comunidad no existe");
        }
        // Verificar que el usuario es el propietario de la comunidad
        User owneractual=userService.getUserbyId(existingCommunity.getOwner().getId());

        if (owner==null)
        {
            throw new NullPointerException("El usuario no existe");
        }
       if (owneractual.getId() != owner.getId()) {
           throw new AccessDeniedException("No tienes permiso para eliminar esta comunidad");
        }
        communityService.deleteById(id);

        return new ResponseEntity<>("Comunidad Eliminada",  HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','PARTICIPANT')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCommunity(@PathVariable("id") int id, @RequestBody CommunityRequestDTO community) throws Exception {
        // Verificar si la comunidad existe
        CommunityResponseDTO existingCommunity = communityService.findById(id);
        if (existingCommunity == null) {
            throw new EntityNotFoundException("El id de la comunidad no existe");
        }
        Integer userid= userService.getAuthenticatedUserIdFromJWT();
        User owner= userService.getUserbyId(userid);

        // Verificar si el usuario existe
        if (owner == null || owner.getId() == null) {
            throw new EntityNotFoundException("El usuario no existe");
        }

        // Verificar si el usuario es el dueño de la comunidad
        if (existingCommunity.getOwner().getId() != owner.getId()) {
            throw new AccessDeniedException("No tienes permiso para modificar esta comunidad");
        }

        // Actualizar la comunidad
        CommunityResponseDTO updatedCommunity = communityService.update(community, id);
        return new ResponseEntity<>(updatedCommunity, HttpStatus.ACCEPTED);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','PARTICIPANT')")
    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@RequestBody CommunityRequestDTO community) throws Exception {
        Integer userid = userService.getAuthenticatedUserIdFromJWT();
        User owner = userService.getUserbyId(userid);

        // Verificar si 'posts' es null y asignar una lista vacía si lo es
        if (community.getPosts() == null) {
            community.setPosts(new ArrayList<>());  // Asignar lista vacía si 'posts' es null
        }

        communityService.save(community, owner);
        return new ResponseEntity<>("Comunidad creada con éxito", HttpStatus.CREATED);
    }

}
