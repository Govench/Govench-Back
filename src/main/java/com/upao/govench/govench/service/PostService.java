package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import java.util.List;

public interface PostService {
    void publicarPost(int communityId, Post post, User author);
    List<Post> getAllPosts();
    Post findById(int id);
    void deleteById(int id);
    Post save(Post post);
}
