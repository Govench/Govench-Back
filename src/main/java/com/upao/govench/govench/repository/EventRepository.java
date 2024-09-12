package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.tittle = :tittle")
    Optional<Event> findAllByEventTittle(@Param("tittle") String Tittle);

}
