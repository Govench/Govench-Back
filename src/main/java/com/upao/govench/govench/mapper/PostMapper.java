package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.PostRequestDTO;
import com.upao.govench.govench.model.dto.PostResponseDTO;
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
        return modelMapper.map(post, PostResponseDTO.class);
    }

    public List<PostResponseDTO> convertToListDTO(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
