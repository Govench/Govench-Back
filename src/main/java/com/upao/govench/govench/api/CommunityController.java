package com.upao.govench.govench.api;

import com.upao.govench.govench.mapper.CommunityMapper;
import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.dto.OwnerRequestDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.EncryptionService;
import com.upao.govench.govench.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequestMapping("/community")
@RestController
public class CommunityController {
    @Autowired
    private EncryptionService encryptionService; // Inyección del servicio de encriptación
    @Autowired
    private CommunityService communityService; // Inyección del servicio de Community
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommunityMapper communityMapper;


    @GetMapping
    public List<CommunityResponseDTO> getCommunitiesByUser(@RequestParam int userId) throws Exception {
        return communityService.findByOwner_Id(userId);
    }


    @GetMapping("/others")
    public List<CommunityResponseDTO> getCommunitiesNotCreatedByUser(@RequestParam int userId) throws Exception {
        return communityService.findByOwner_IdNot(userId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable("id") int id, @RequestBody OwnerRequestDTO ownerdto) throws Exception {
        CommunityResponseDTO existingCommunity = communityService.findById(id);
        if (existingCommunity == null) {
            throw new NullPointerException("Esta comunidad no existe");
        }
        // Verificar que el usuario es el propietario de la comunidad
        User owneractual=userService.getUserbyId(existingCommunity.getOwner().getId());

        if (ownerdto==null)
        {
            throw new NullPointerException("El usuario no existe");
        }
       if (owneractual.getOrganizer().getId() != ownerdto.getId()) {
           throw new AccessDeniedException("No tienes permiso para eliminar esta comunidad");
        }
        communityService.deleteById(id);

        return new ResponseEntity<>("Comunidad Eliminada",  HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public CommunityResponseDTO getCommunityById(@PathVariable("id") int id)  {
        return  communityService.findById(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommunity(@PathVariable("id") int id, @RequestBody CommunityRequestDTO community) throws Exception {
        // Verificar si la comunidad existe
        CommunityResponseDTO existingCommunity = communityService.findById(id);
        if (existingCommunity == null) {
            throw new EntityNotFoundException("El id de la comunidad no existe");
        }

        User owner = community.getOwner();

        // Verificar si el usuario existe
        if (owner == null || owner.getOrganizer().getId() == null) {
            throw new EntityNotFoundException("El usuario no existe");
        }

        // Verificar si el usuario es el dueño de la comunidad
        if (existingCommunity.getOwner().getId() != owner.getOrganizer().getId()) {
            throw new AccessDeniedException("No tienes permiso para modificar esta comunidad");
        }

        // Actualizar la comunidad
        CommunityResponseDTO updatedCommunity = communityService.update(community, id);
        return new ResponseEntity<>(updatedCommunity, HttpStatus.ACCEPTED);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@RequestBody CommunityRequestDTO community) throws Exception {
        User owner= community.getOwner();
        communityService.save(community, owner);
        return new ResponseEntity<>("Comunidad creada con éxito", HttpStatus.CREATED);
    }

}
