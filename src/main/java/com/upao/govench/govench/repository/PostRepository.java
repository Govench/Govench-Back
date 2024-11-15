package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.PostResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.upao.govench.govench.model.entity.Post;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.comunidad.id = :communityId")
    List<Post> findPostsByCommunityId(@Param("communityId") int communityId);
    int countByAutor_IdAndCreated(Integer autorId, LocalDate created);
    List<Post> findPostByAutor_Id(Integer autorId);
}

