package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.repository.PostRepository;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Override
    public void publicarPost(int communityId, Post post, User author) {
        Optional<Community> communityOptional = communityRepository.findById(communityId);
        if (communityOptional.isPresent()) {
            Community community = communityOptional.get();
            post.setAutor(author);
            post.setComunidad(community);
            post.setCreated(new Date());
            postRepository.save(post);
        } else {
            throw new RuntimeException("La comunidad no fue encontrada.");
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }
}

