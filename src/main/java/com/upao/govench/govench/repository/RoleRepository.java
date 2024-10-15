package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRole_name(String name);
}
