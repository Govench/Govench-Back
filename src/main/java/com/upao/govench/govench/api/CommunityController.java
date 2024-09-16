package com.upao.govench.govench.api;

import com.upao.govench.govench.model.entity.Community;
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
    private CommunityService CommunityService; // Inyección del servicio de Community
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Community> getCommunitiesByUser(@PathVariable("encodedUserId") String encodedUserId) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        return CommunityService.findByOwner_Id(userId);
    }

    // Obtener comunidades que no fueron creadas por el usuario
    @GetMapping("/others")
    public List<Community> getCommunitiesNotCreatedByUser(@PathVariable("encodedUserId") String encodedUserId) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        return CommunityService.findByOwner_IdNot(userId);
    }
    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("id") int id) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));

        Community existingCommunity = CommunityService.findById(id);

        // Verificar que el usuario es el propietario de la comunidad
       if (existingCommunity.getOwner().getId() != userId) {
           throw new AccessDeniedException("No tienes permiso para eliminar esta comunidad");
        }
        CommunityService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Community getCommunityById(@PathVariable("id") int id)  {
        return CommunityService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCommunity(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("id") int id, @RequestBody Community community) throws Exception
    {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));

        // Obtener la comunidad por su ID
        Community existingCommunity = CommunityService.findById(id);

        // Verificar que el usuario es el propietario de la comunidad
          if (existingCommunity.getOwner().getId() != userId) {
          throw new AccessDeniedException("No tienes permiso para modificar esta comunidad");
        }
        
        // Si es el propietario, permite la actualización
        CommunityService.update(community, id);
        return new ResponseEntity<>("Comunidad editada con éxito", HttpStatus.ACCEPTED);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createCommunity(@PathVariable("encodedUserId") String encodedUserId,@RequestBody Community community) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        User owner=userService.getUserbyId(userId);
        CommunityService.save(community,owner);
        return new ResponseEntity<>("Comunidad creada con éxito", HttpStatus.CREATED);

    }
}
