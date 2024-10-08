package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByRatedUserId(Integer ratedUserId);
}
