package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.UserBasicDTO;
import com.upao.govench.govench.model.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
    Optional<Organizer> findByNameAndLastname(String nombre, String last_name);
    boolean existsByNameAndLastname(String name, String last_name);
    boolean existsByNameAndLastnameAndUserIdNot(String email, String last_name,int userId);
    @Query("SELECT new com.upao.govench.govench.model.dto.UserBasicDTO(o.id, o.name, o.user.email) " +
            "FROM Organizer o WHERE o.user.id = :userId")
    Optional<UserBasicDTO> findOrganizerDtoByUserId(@Param("userId") Integer userId);

}
