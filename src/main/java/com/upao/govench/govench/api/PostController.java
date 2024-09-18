package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.PostDTO;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.EncryptionService;
import com.upao.govench.govench.service.PostService;
import com.upao.govench.govench.service.UserService;
import com.upao.govench.govench.service.impl.PostServiceImpl;
import org.hibernate.validator.internal.constraintvalidators.bv.number.sign.PositiveValidatorForBigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;
    @Autowired
    private CommunityService communityService;

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Post post) {

        int userId = post.getAutor().getId();
        User author = userService.getUserbyId(userId);

        if (author == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        
        int communityId = post.getComunidad().getId();
        Community community = communityService.findById(communityId);

        if (community == null) {
            return new ResponseEntity<>("Comunidad no encontrada", HttpStatus.NOT_FOUND);
        }
        post.setAutor(author);
        post.setComunidad(community);
        post.setCreated(new Date());

        postService.save(post);
        return new ResponseEntity<>("Post creado con éxito", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> obtenerTodosLosPosts() {
        List<Post> posts = postService.getAllPosts();


        List<PostDTO> postDTOs = posts.stream().map(post -> new PostDTO(
                        post.getId(),
                        post.getBody(),
                        post.getAutor().getName(),
                        post.getComunidad().getNombre(),
                        post.getCreated()
                )
        ).collect(Collectors.toList());

        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }


    @DeleteMapping("/{encodedUserId}/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("postId") int postId) throws Exception {
        try {
            int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));

            Post existingPost = postService.findById(postId);

            if (existingPost == null) {
                return new ResponseEntity<>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

            if (existingPost.getAutor().getId() != userId) {
                throw new AccessDeniedException("No tienes permiso para eliminar este post");
            }

            postService.deleteById(postId);

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
        public ResponseEntity<?> updatePost(@PathVariable("encodedUserId") String encodedUserId, @PathVariable("postId") int postId) throws Exception {
            try {
                int userId = Integer.parseInt(encryptionService.decrypt(encodedUserId));
                Post updatedPost = postService.findById(postId);
                 System.out.println("User ID desencriptado: " + userId);

                Post updatedExistingPost = postService.actualizaPost(postId, updatedPost);

            if (updatedExistingPost == null) {
                return new ResponseEntity<>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

            if (updatedExistingPost.getAutor().getId() != userId) {
                throw new AccessDeniedException("No tienes permiso para modificar este post");
            }

            return new ResponseEntity<>("El post ha sido actualizado", HttpStatus.ACCEPTED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Error al desencriptar el ID de usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}



