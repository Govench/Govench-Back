package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.PostMapper;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.service.CommunityService;
import com.upao.govench.govench.service.PostService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class PostController {
    @Autowired
    private final PostService postServiceImpl;
    @Autowired
    private final UserService userService;
    @Autowired
    private final CommunityService communityService;
    @Autowired
    private final PostService postService;
    @Autowired
    private PostMapper postMapper;

    @PostMapping("/community/{communityId}/posts/create")
    public ResponseEntity<Map<String, String>> createPost(@PathVariable("communityId") int communityId,
                                                          @RequestBody PostRequestDTO postRequestDTO) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        if (userId == null) {
            return new ResponseEntity<>(Map.of("message", "Acceso denegado: Usuario no autenticado"), HttpStatus.FORBIDDEN);
        }

        User author = userService.getUserbyId(userId);

        if (author == null) {
            return new ResponseEntity<>(Map.of("message", "Usuario no encontrado"), HttpStatus.NOT_FOUND);
        }

        Community community = communityService.EntityfindById(communityId);
        if (community == null) {
            return new ResponseEntity<>(Map.of("message", "Comunidad no encontrada"), HttpStatus.NOT_FOUND);
        }

        // Usar el PostMapper para crear la entidad Post
        Post post = postMapper.toEntity(postRequestDTO, author, community);

        // Guardar el post
        postServiceImpl.publicarPost(post);

        Map<String, String> response = Map.of("body", post.getBody());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponseDTO> obtenerPostPorId(@PathVariable("postId") int postId) {
        try {
            PostResponseDTO post = postServiceImpl.getPostById(postId);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Autorización de Organizador y Participante


    @GetMapping("/community/{communityId}/posts")
    public ResponseEntity<List<PostResponseDTO>> obtenerPostsPorComunidadId(@PathVariable("communityId") int communityId) {
        try {
            List<PostResponseDTO> posts = postService.getPostsByCommunityId(communityId);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/community/{communityId}/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("communityId") int communityId,
                                             @PathVariable("postId") int postId) {
        try {
            // Obtener el ID del usuario autenticado desde el JWT
            Integer userId = userService.getAuthenticatedUserIdFromJWT();

            // Verificar que el usuario esté autenticado
            if (userId == null) {
                return new ResponseEntity<>("Acceso denegado: Usuario no autenticado", HttpStatus.FORBIDDEN);
            }

            // Obtener el post existente usando la entidad completa
            Post existingPost = postServiceImpl.getPostEntityById(postId);

            if (existingPost == null) {
                return new ResponseEntity<>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

            // Verificar que el post pertenezca a la comunidad especificada
            if (existingPost.getComunidad().getId() != communityId) {
                return new ResponseEntity<>("El post no pertenece a esta comunidad", HttpStatus.BAD_REQUEST);
            }

            // Verificar que el autor sea el mismo que el usuario autenticado
            if (existingPost.getAutor().getId() != userId) {
                return new ResponseEntity<>("No tienes permiso para eliminar este post", HttpStatus.FORBIDDEN);
            }

            // Eliminar el post
            postServiceImpl.deleteById(postId);

            return new ResponseEntity<>("Post eliminado con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/community/{communityId}/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("communityId") int communityId,
                                             @PathVariable("postId") int postId,
                                             @RequestBody PostRequestDTO postRequestDTO) {
        try {
            // Obtener el ID del usuario autenticado desde el JWT
            Integer userId = userService.getAuthenticatedUserIdFromJWT();

            // Verificar que el usuario esté autenticado
            if (userId == null) {
                return new ResponseEntity<>("Acceso denegado: Usuario no autenticado", HttpStatus.FORBIDDEN);
            }

            // Obtener el post existente usando la entidad completa
            Post existingPost = postServiceImpl.getPostEntityById(postId);

            if (existingPost == null) {
                return new ResponseEntity<>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

            // Verificar que el post pertenezca a la comunidad especificada
            if (existingPost.getComunidad().getId() != communityId) {
                return new ResponseEntity<>("El post no pertenece a esta comunidad", HttpStatus.BAD_REQUEST);
            }

            // Verificar que el autor sea el mismo que el usuario autenticado
            if (existingPost.getAutor().getId() != userId) {
                return new ResponseEntity<>("No tienes permiso para modificar este post", HttpStatus.FORBIDDEN);
            }

            // Actualizar el cuerpo del post
            existingPost.setBody(postRequestDTO.getBody());
            existingPost.setUpdated(LocalDateTime.now());

            // Guardar los cambios
            postServiceImpl.save(existingPost);

            return new ResponseEntity<>("El post ha sido actualizado", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/mypost")
    public ResponseEntity<List<PostResponseDTO>> obtenerMyPosts() {
        return new ResponseEntity<>(postService.getAllPostsByUserId(), HttpStatus.OK);
    }

}

