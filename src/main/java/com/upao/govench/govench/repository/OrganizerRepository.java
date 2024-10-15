package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
    Optional<Organizer> findByNameAndLast_name(String nombre, String last_name);
    boolean existsByNameAndLast_name(String name, String last_name);
    boolean existsByNameAndLast_nameAndUserIdNot(String email, String last_name,int userId);
}
