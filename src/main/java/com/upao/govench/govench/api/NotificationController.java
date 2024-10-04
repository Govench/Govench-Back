package com.upao.govench.govench.api;

import lombok.AllArgsConstructor;
import com.upao.govench.govench.service.UserEventService;
import com.upao.govench.govench.model.dto.ReminderRequestDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;
    private final UserEventService userEventService;

    @PostMapping("/trigger-reminders")
    public ResponseEntity<String> triggerReminders(@RequestBody ReminderRequestDTO request) {
        User user = request.getUser();
        Event event = request.getEvent();

        UserEvent userEvent = userEventService.getUserEventbyUser(user).stream()
                .filter(ue -> ue.getEvent().equals(event))
                .findFirst()
                .orElse(null);

        if (userEvent != null && userEvent.isNotificationsEnabled()) {
        notificationService.scheduleEventReminders(user, event);
        return ResponseEntity.ok("Reminders triggered");
        } else {
            return ResponseEntity.status(403).body("Notifications are not enabled for this user or event.");
        }
    }

    @PutMapping("/update-notifications")
    public ResponseEntity<String> updateNotifications(@RequestBody UserEvent userEvent) {
        User user = userEvent.getUser();
        Event event = userEvent.getEvent();
        boolean enableNotifications = userEvent.isNotificationsEnabled();

        if (user == null || event == null) {
            return ResponseEntity.status(400).body("User or Event is missing.");
        }

        UserEvent existingUserEvent = userEventService.getUserEventbyUser(user).stream()
                .filter(ue -> ue.getEvent().getId() == event.getId())
                .findFirst()
                .orElse(null);

        if (existingUserEvent != null) {
            existingUserEvent.setNotificationsEnabled(enableNotifications);
            userEventService.addUserEvent(existingUserEvent);
            String status = enableNotifications ? "activated" : "deactivated";
            return ResponseEntity.ok("Notifications " + status + " for event ID: " + event.getId());
        } else {
            return ResponseEntity.status(404).body("UserEvent not found.");
        }
    }
}