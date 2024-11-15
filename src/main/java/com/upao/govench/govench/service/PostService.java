package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.PostRequestDTO;
import com.upao.govench.govench.model.dto.PostResponseDTO;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;

import java.util.List;

public interface PostService {

    void publicarPost(int communityId, PostRequestDTO postRequestDTO, User author);
    List<PostResponseDTO> getAllPosts();
    void deleteById(int id);
    PostResponseDTO getPostById(int id);
    PostResponseDTO actualizaPost(int id, PostRequestDTO postRequestDTO);

    List<PostResponseDTO> getPostsByCommunityId(int communityId);

    Post getPostEntityById(int postId);

    void save(Post existingPost);

    void publicarPost(Post post); // Cambiar el parámetro a tipo Post

    List<PostResponseDTO> getAllPostsByUserId();
}

