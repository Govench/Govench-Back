package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.PostRequestDTO;
import com.upao.govench.govench.model.dto.PostResponseDTO;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.Participant;
import java.util.List;

public interface PostService {

    void publicarPost(int communityId, PostRequestDTO postRequestDTO, Participant author);
    List<PostResponseDTO> getAllPosts();
    void deleteById(int id);
    PostResponseDTO getPostById(int id);
    PostResponseDTO actualizaPost(int id, PostRequestDTO postRequestDTO);

}
