package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Follow;
import com.upao.govench.govench.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    List<Follow> findByFollowing(User following);

    List<Follow> findByFollower(User follower);

    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);

}
