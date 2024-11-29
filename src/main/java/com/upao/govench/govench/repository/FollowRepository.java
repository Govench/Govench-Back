package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Follow;
import com.upao.govench.govench.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    List<Follow> findByFollowing(User following);

    List<Follow> findByFollower(User follower);

    boolean existsByFollowerAndFollowing(User follower, User following);

    Follow findByFollowerAndFollowing(User follower, User following);

    List<Follow> findByFollowing_Id(Integer Idfollowing);
    List<Follow> findByFollower_Id(Integer Idfollower);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :Idfollowing")
    int countByFollowingId(@Param("Idfollowing") Integer IdFollowing);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :Idfollower")
    int countByFollowerId(@Param("Idfollower") Integer IdFollower);

}
