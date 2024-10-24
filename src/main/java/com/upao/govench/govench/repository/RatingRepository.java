package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findAllById(Integer ratedUserId);
    @Query("SELECT COUNT(r) FROM Rating r JOIN r.ratedOrganizer o WHERE o.id IN (SELECT ue.event.owner.id FROM UserEvent ue WHERE ue.user.id = :userId)")
    int countRatingsForAttendedEvents(@Param("userId") Integer userId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.ratedOrganizer.id IN (SELECT e.owner.id FROM Event e WHERE e.owner.id = :userId)")
    int countRatingsForCreatedEvents(@Param("userId") Integer userId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.ratedParticipant.id = :userId OR r.ratedOrganizer.id = :userId")
    int countRatingsReceivedByUserId(@Param("userId") Integer userId);
}
