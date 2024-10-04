package com.upao.govench.govench.api;

import com.upao.govench.govench.mapper.CommunityMapper;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.EncryptionService;
import com.upao.govench.govench.service.PostService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    @Autowired
    private final PostService postServiceImpl;
    @Autowired
    private final UserService userService;
    @Autowired
    private final CommunityService communityService;
    @Autowired
    private final EncryptionService encryptionService;
    @Autowired
    private final CommunityMapper communityMapper;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostRequestDTO postRequestDTO) {

        int userId = postRequestDTO.getAutor().getId();
        User author = userService.getUserbyId(userId);

        if (author == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        int communityId = postRequestDTO.getComunidad().getId();

        Community community = communityService.EntityfindById(communityId);
        if (community == null) {
            return new ResponseEntity<>("Comunidad no encontrada", HttpStatus.NOT_FOUND);
        }
        postRequestDTO.setAutor(author);
        postRequestDTO.setComunidad(community);
        postRequestDTO.setCreated(LocalDate.now());

        postServiceImpl.publicarPost(communityId,postRequestDTO,author);
        return new ResponseEntity<>("Post creado con éxito", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> obtenerTodosLosPosts() {
        List<PostResponseDTO> posts = postServiceImpl.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


        @DeleteMapping("/{encodedUserId}/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("postId") int postId) throws Exception {
        try {
            int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));

            PostResponseDTO existingPost = postServiceImpl.getPostById(postId);

            if (existingPost == null) {
                return new ResponseEntity<>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

            if (existingPost.getAutor().getId() != userId) {
                throw new AccessDeniedException("No tienes permiso para eliminar este post");
            }

            postServiceImpl.deleteById(postId);

            return new ResponseEntity<>("Post eliminado con éxito", HttpStatus.OK);
        }

        catch (NumberFormatException e) {
            return new ResponseEntity<>("Error al desencriptar el ID de usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{encodedUserId}/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("postId") int postId,
                                        @RequestBody PostRequestDTO postRequestDTO) throws Exception {
        try {
            int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
            PostResponseDTO Post1 = postServiceImpl.getPostById(postId);
            System.out.println("User ID desencriptado: " + userId);

            if (Post1 == null) {
                return new ResponseEntity<>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

            if (Post1.getAutor().getId() != userId) {
                throw new AccessDeniedException("No tienes permiso para modificar este post");
            }

            PostResponseDTO updatedPost = postServiceImpl.actualizaPost(postId, postRequestDTO);
            updatedPost.setUpdated(LocalDateTime.now());

            return new ResponseEntity<>("El post ha sido actualizado", HttpStatus.ACCEPTED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Error al desencriptar el ID de usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}

