package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Collection;
import com.upao.govench.govench.model.entity.CollectionEvent;
import com.upao.govench.govench.model.entity.CollectionEventPK;
import com.upao.govench.govench.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface CollectionEventRepository extends JpaRepository<CollectionEvent, CollectionEventPK> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO collections_events (event_id, collection_id, added_date) " +
            "VALUES (:eventId,:collectionId,:addedDate)", nativeQuery = true)
    void addEventToCollection(@Param("eventId") Integer eventId,
                              @Param("collectionId") Integer collectionId,
                              @Param("addedDate") LocalDateTime addedDate);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM collections_events " +
                    "WHERE event_id = :eventId " +
                    "AND collection_id = :collectionId", nativeQuery = true)
    void deleteByEventAndCollection(@Param("eventId") Integer eventId,
                                    @Param("collectionId") Integer collectionId);

    @Query("SELECT ce.event FROM CollectionEvent ce WHERE ce.collection = :collection")
    List<Event> findEventsByCollection(Collection collection);


    Boolean existsByCollectionAndEvent(Collection collection, Event event);
}