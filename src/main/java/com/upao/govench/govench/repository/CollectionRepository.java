package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    List<Collection> findByUserId(Integer userId);
}
