package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.IdCompuestoU_C;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCommunityRepository extends JpaRepository<UserCommunity, IdCompuestoU_C> {
    List<UserCommunity> findByUser(User user);
    Optional<UserCommunity> findByUserIdAndCommunityId(int userId, int communityId);
}
