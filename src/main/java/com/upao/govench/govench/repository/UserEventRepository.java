package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.model.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, IdCompuestoU_E> {

}
