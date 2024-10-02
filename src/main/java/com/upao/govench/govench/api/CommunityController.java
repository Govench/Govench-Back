package com.upao.govench.govench.api;

import com.upao.govench.govench.mapper.CommunityMapper;
import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.EncryptionService;
import com.upao.govench.govench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequestMapping("/{encodedUserId}/community")
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
    public List<CommunityResponseDTO> getCommunitiesByUser(@PathVariable("encodedUserId") String encodedUserId) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        return communityService.findByOwner_Id(userId);
    }


    @GetMapping("/others")
    public List<CommunityResponseDTO> getCommunitiesNotCreatedByUser(@PathVariable("encodedUserId") String encodedUserId) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        return communityService.findByOwner_IdNot(userId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("id") int id) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));

        CommunityResponseDTO existingCommunity = communityService.findById(id);
        if (existingCommunity == null) {
            throw new NullPointerException("Esta comunidad no existe");
        }
        // Verificar que el usuario es el propietario de la comunidad
        User owner=userService.getUserbyId(userId);

        if (owner.getId()==null)
        {
            throw new NullPointerException("El usuario no existe");
        }
       if (owner.getId() != userId) {
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
    public ResponseEntity<?> updateCommunity(
            @PathVariable("encodedUserId") String encodedUserId,
            @PathVariable("id") int id,
            @RequestBody CommunityRequestDTO community
    ) throws Exception {
        // Verificar si la comunidad existe
        if(communityService.findById(id) == null) {
            throw new NullPointerException("El id de la comunidad no existe");
        }

        // Desencriptar el userId
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        User owner = userService.getUserbyId(userId);

        // Verificar si el usuario existe
        if (owner == null || owner.getId() == null) {
            throw new NullPointerException("El usuario no existe");
        }

        // Verificar si el usuario es el dueño de la comunidad
        if (owner.getId() != userId) {
            throw new AccessDeniedException("No tienes permiso para modificar esta comunidad");
        }

        return new ResponseEntity<>(communityService.update(community, id), HttpStatus.ACCEPTED);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@PathVariable("encodedUserId") String encodedUserId,@RequestBody CommunityRequestDTO community) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        User owner=userService.getUserbyId(userId);
        communityService.save(community,owner);
        return new ResponseEntity<>("Comunidad creada con éxito", HttpStatus.CREATED);
    }

}
