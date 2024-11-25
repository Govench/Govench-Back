package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.UserBasicDTO;
import com.upao.govench.govench.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

boolean existsByNameAndLastname(String name, String last_name);
boolean existsByNameAndLastnameAndUserIdNot(String email, String last_name,int userId);
    @Query("SELECT new com.upao.govench.govench.model.dto.UserBasicDTO(p.id, p.name, p.user.email) " +
            "FROM Participant p WHERE p.user.id = :userId")
    Optional<UserBasicDTO> findParticipantDtoByUserId(@Param("userId") Integer userId);

}
