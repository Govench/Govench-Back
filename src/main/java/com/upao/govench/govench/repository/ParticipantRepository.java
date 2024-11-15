package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

boolean existsByNameAndLastname(String name, String last_name);
boolean existsByNameAndLastnameAndUserIdNot(String email, String last_name,int userId);
}
