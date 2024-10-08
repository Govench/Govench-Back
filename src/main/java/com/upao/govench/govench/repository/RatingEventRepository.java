package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.RatingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingEventRepository extends JpaRepository<RatingEvent, Integer> {
    List<RatingEvent> findByEventId(int eventId);
}
