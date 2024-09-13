package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
