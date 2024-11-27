package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.EventBasicDTO;
import com.upao.govench.govench.model.dto.ReportResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE e.date >= :date")
    List<Event> findAllUpcomingEvents(@Param("date") LocalDate date);

    @Query("SELECT e FROM Event e WHERE e.tittle = :tittle")
    List<Event> findAllByEventTittle(@Param("tittle") String Tittle);

    @Query("SELECT e FROM Event e WHERE e.exp = :exp")
    List<Event> findAllByEventExp(@Param("exp") String Exp);

    @Query("SELECT e FROM Event e JOIN UserEvent ue ON e.id = ue.event.id WHERE ue.user.id = :userId AND e.date >= :now")
    List<Event> findUpcomingEventsForUser(@Param("userId") Integer userId, @Param("now") LocalDate now);

    List<Event> findByOwner_Id(Integer ownerId);

    @Query("SELECT new com.upao.govench.govench.model.dto.EventBasicDTO(e.id, e.tittle, e.date) " +
            "FROM Event e WHERE e.owner.id = :ownerId")
    List<EventBasicDTO> findSimplifiedEventsByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT e.tittle, COUNT(ue.id) " +
            "FROM Event e LEFT JOIN UserEvent ue ON e.id = ue.event.id " +
            "WHERE e.owner.id = :userId " +
            "GROUP BY e.tittle " +
            "ORDER BY COUNT(ue.id) DESC")
    List<Object[]> findEventsWithParticipantsCount(@Param("userId") Integer userId);

    List<Event> findByDeletedFalse();
}
