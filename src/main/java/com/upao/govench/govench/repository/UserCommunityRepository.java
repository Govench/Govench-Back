package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.IdCompuestoU_C;
import com.upao.govench.govench.model.entity.UserCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCommunityRepository extends JpaRepository<UserCommunity, IdCompuestoU_C> {

}
