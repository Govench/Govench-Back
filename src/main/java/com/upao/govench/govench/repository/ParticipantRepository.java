package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

boolean existsByNameAndLast_name(String name, String last_name);
boolean existsByNameAndLast_nameAndUserIdNot(String email, String last_name,int userId);
}
