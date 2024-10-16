package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Integer> {
    // Encuentra todas las comunidades creadas por un usuario específico
    List<Community> findByOwner(User owner);

    // Encuentra todas las comunidades que no fueron creadas por un usuario específico
    List<Community> findByOwnerNot(User owner);

    // Encuentra todas las comunidades por el ID de propietario (user_id)
    List<Community> findByOwner_id(int userId);

    // Encuentra todas las comunidades que no fueron creadas por un ID de propietario (user_id)
    List<Community> findByOwner_idNot(int userId);
}
