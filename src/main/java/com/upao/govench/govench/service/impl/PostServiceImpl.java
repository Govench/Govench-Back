package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.PostMapper;
import com.upao.govench.govench.model.dto.PostRequestDTO;
import com.upao.govench.govench.model.dto.PostResponseDTO;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.repository.PostRepository;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.service.PostService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private PostRepository postRepository;
    private CommunityRepository communityRepository;
    private PostMapper postMapper;

    @Transactional
    public void publicarPost(int communityId, PostRequestDTO postRequestDTO, User author) {
        Optional<Community> communityOptional = communityRepository.findById(communityId);
        Post post = postMapper.convertToEntity(postRequestDTO);
        if (communityOptional.isPresent()) {
            Community community = communityOptional.get();
            post.setAutor(author);
            post.setComunidad(community);
            post.setCreated(LocalDate.now());
            postRepository.save(post);
        } else {
            throw new RuntimeException("La comunidad no fue encontrada.");
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return postMapper.convertToListDTO(posts);
    }

    @Transactional
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostResponseDTO getPostById(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post no encontrado con el id" + id));
        return postMapper.convertToDTO(post);
    }

    @Transactional
    public PostResponseDTO actualizaPost(int id, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con el numero de id" + id));
        if(postRequestDTO.getBody()!=null)post.setBody(postRequestDTO.getBody());

        post = postRepository.save(post);

        return postMapper.convertToDTO(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByCommunityId(int communityId) {
        List<Post> posts = postRepository.findPostsByCommunityId(communityId);
        return postMapper.convertToListDTO(posts);
    }

    public Post getPostEntityById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public void publicarPost(Post post) {
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPostsByUserId() {
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        List<Post> posts = postRepository.findPostByAutor_Id(userId);
        return postMapper.convertToListDTO(posts);
    }

}

