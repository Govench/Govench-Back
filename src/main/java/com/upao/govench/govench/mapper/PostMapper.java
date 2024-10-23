// PostMapper.java
package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Post;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

        // Create a UserBasicDTO for the author
        UserBasicDTO userBasicDTO = new UserBasicDTO();
        userBasicDTO.setId(post.getAutor().getId());
        userBasicDTO.setName(post.getAutor().getParticipant() != null ?
                post.getAutor().getParticipant().getName() :
                post.getAutor().getOrganizer() != null ?
                        post.getAutor().getOrganizer().getName() :
                        "Unknown");
        userBasicDTO.setEmail(post.getAutor().getEmail());

        // Assign the UserBasicDTO to the PostResponseDTO
        postResponseDTO.setAutor(userBasicDTO);
        return postResponseDTO;
    }

    public List<PostResponseDTO> convertToListDTO(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
