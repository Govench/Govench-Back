package com.upao.govench.govench.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.upao.govench.govench.model.entity.Post;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.comunidad.id = :communityId")
    List<Post> findPostsByCommunityId(@Param("communityId") int communityId);
    @Query("SELECT COUNT(p) FROM Post p JOIN p.comunidad c WHERE c.owner.id = :userId")
    int countPostsInCommunitiesCreatedByUser(@Param("userId") Integer userId);

}

