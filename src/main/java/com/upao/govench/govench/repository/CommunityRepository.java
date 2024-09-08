package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Integer> {
    // Encuentra todas las comunidades creadas por un usuario específico
    List<Community> findByOwner_Id(int userId);

    // Encuentra todas las comunidades que no fueron creadas por un usuario específico
    List<Community> findByOwner_IdNot(int userId);
}
