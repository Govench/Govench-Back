package com.upao.govench.govench.api;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.Post;
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



    @GetMapping
    public List<Community> getCommunitiesByUser(@PathVariable("encodedUserId") String encodedUserId) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        return communityService.findByOwner_Id(userId);
    }


    @GetMapping("/others")
    public List<Community> getCommunitiesNotCreatedByUser(@PathVariable("encodedUserId") String encodedUserId) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        return communityService.findByOwner_IdNot(userId);
    }
    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("id") int id) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));

        Community existingCommunity = communityService.findById(id);

        // Verificar que el usuario es el propietario de la comunidad
       if (existingCommunity.getOwner().getId() != userId) {
           throw new AccessDeniedException("No tienes permiso para eliminar esta comunidad");
        }
        communityService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Community getCommunityById(@PathVariable("id") int id)  {
        return communityService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCommunity(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("id") int id, @RequestBody Community community) throws Exception
    {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));


        Community existingCommunity = communityService.findById(id);


          if (existingCommunity.getOwner().getId() != userId) {
          throw new AccessDeniedException("No tienes permiso para modificar esta comunidad");
        }
        

        communityService.update(community, id);
        return new ResponseEntity<>("Comunidad editada con éxito", HttpStatus.ACCEPTED);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createCommunity(@PathVariable("encodedUserId") String encodedUserId,@RequestBody Community community) throws Exception {
        int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
        User owner=userService.getUserbyId(userId);
        communityService.save(community,owner);
        return new ResponseEntity<>("Comunidad creada con éxito", HttpStatus.CREATED);

    }

}
