package com.upao.govench.govench.api;

import com.upao.govench.govench.model.entity.Participant;
import com.upao.govench.govench.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import com.upao.govench.govench.service.UserEventService;
import com.upao.govench.govench.model.dto.ReminderRequestDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;
    private final UserEventService userEventService;
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'PARTICIPANT')")
    @PutMapping("/update/{eventId}")
    public ResponseEntity<String> updateNotifications(@PathVariable("eventId") Long eventId,
                                                      @RequestParam("enableNotifications") boolean enableNotifications) {

        // Obtener el usuario autenticado
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        User owner = userService.getUserbyId(userId);

        // Verificar si el evento existe en la relación del usuario
        UserEvent existingUserEvent = userEventService.getUserEventbyUserIdAndEventId(userId, eventId);
        if (existingUserEvent == null) {
            throw new EntityNotFoundException("No se encontró una relación entre el usuario y el evento.");
        }

        // Verificar si el usuario es el propietario de la relación UserEvent
        if (!existingUserEvent.getUser().getId().equals(owner.getId())) {
            throw new AccessDeniedException("No tienes permiso para modificar las notificaciones de este evento.");
        }

        // Actualizar la notificación según el estado deseado
        existingUserEvent.setNotificationsEnabled(enableNotifications);
        userEventService.updateUserEvent(existingUserEvent);
        String status = enableNotifications ? "activadas" : "desactivadas";
        return ResponseEntity.ok("Notificaciones " + status + " para el evento ID: " + eventId);
    }
}