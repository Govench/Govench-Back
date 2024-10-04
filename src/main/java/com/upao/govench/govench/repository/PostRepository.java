package com.upao.govench.govench.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.upao.govench.govench.model.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}

