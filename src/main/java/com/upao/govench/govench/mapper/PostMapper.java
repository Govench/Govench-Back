// PostMapper.java
package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class PostMapper {

    private final ModelMapper modelMapper;

    public Post convertToEntity(PostRequestDTO postRequestDTO) {
        return modelMapper.map(postRequestDTO, Post.class);
    }

    public PostResponseDTO convertToDTO(Post post) {
        PostResponseDTO postResponseDTO = modelMapper.map(post, PostResponseDTO.class);

        // Crear manualmente el UserBasicDTO para el autor
        UserBasicDTO userBasicDTO = new UserBasicDTO();
        userBasicDTO.setId(post.getAutor().getId());
        userBasicDTO.setEmail(post.getAutor().getEmail());

        if (post.getAutor().getParticipant() != null) {

            userBasicDTO.setName(post.getAutor().getParticipant().getName());
        }
        else if (post.getAutor().getOrganizer() != null) {
            userBasicDTO.setName(post.getAutor().getOrganizer().getName());
        } else {
            userBasicDTO.setName("Desconocido");
        }

        // Asignar el UserBasicDTO al PostResponseDTO
        postResponseDTO.setAutor(userBasicDTO);

        // Asignar el id de la comunidad manualmente
        if (post.getComunidad() != null) {
            postResponseDTO.setComunidadId(post.getComunidad().getId());
        }

        return postResponseDTO;
    }

    public List<PostResponseDTO> convertToListDTO(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Post toEntity(PostRequestDTO postRequestDTO, User author, Community community) {
        Post post = new Post();
        post.setBody(postRequestDTO.getBody());
        post.setAutor(author);
        post.setComunidad(community);
        post.setCreated(LocalDate.now());
        return post;
    }
}
