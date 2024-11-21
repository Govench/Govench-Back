package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.RatingEvent;
import com.upao.govench.govench.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingEventRepository extends JpaRepository<RatingEvent, Integer> {
    List<RatingEvent> findRatingEventByEventId(Event eventId);

    @Query("SELECT r FROM RatingEvent r WHERE r.eventId.id = :eventId")
    List<RatingEvent> findRatingsByEventId(@Param("eventId") int eventId);

    int countByEventId_IdAndValorPuntuacion(Integer eventId, Integer valorPuntuacion);

    @Query("SELECT new com.upao.govench.govench.model.dto.RatingEventResponseDTO(" +
            "r.id, r.valorPuntuacion, r.fechaPuntuacion, r.eventId.tittle) " +
            "FROM RatingEvent r WHERE r.eventId.id = :eventId")
    List<RatingEventResponseDTO> findRatingsWithEventTitleByEventId(@Param("eventId") int eventId);

}
