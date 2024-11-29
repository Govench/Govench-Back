package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, IdCompuestoU_E> {
    List<UserEvent> findByEvent(Event event);
    List<UserEvent> findByUser(User user);


    @Query("SELECT ue FROM UserEvent ue WHERE ue.notificationsEnabled = true")
    List<UserEvent> findUsersWithNotificationsEnabled();

    boolean existsByUserAndEvent(User user, Event event);

    @Query("SELECT ue FROM UserEvent ue WHERE ue.user.id = :userId")
    List<UserEvent> findAllByUserId(@Param("userId") Integer userId);

    @Query("SELECT ue.user FROM UserEvent ue WHERE ue.event.id = :eventId")
    List<User> findUsersByEventId(@Param("eventId") int eventId);

    Optional<UserEvent> findByUserIdAndEventId(Integer userId, Long eventId);

}
