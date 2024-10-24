package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE e.tittle = :tittle")
    List<Event> findAllByEventTittle(@Param("tittle") String Tittle);

    @Query("SELECT e FROM Event e WHERE e.exp = :exp")
    List<Event> findAllByEventExp(@Param("exp") String Exp);

    @Query("SELECT e FROM Event e JOIN UserEvent ue ON e.id = ue.event.id WHERE ue.user.id = :userId AND e.date >= :now")
    List<Event> findUpcomingEventsForUser(@Param("userId") Integer userId, @Param("now") LocalDate now);

    @Query("SELECT COUNT(ue) FROM UserEvent ue WHERE ue.user.id = :userId")
    int countAttendedByUserId(@Param("userId") Integer userId);

    // Contar eventos creados por un usuario específico
    @Query("SELECT COUNT(e) FROM Event e WHERE e.owner.id = :userId")
    int countCreatedByUserId(@Param("userId") Integer userId);
}
