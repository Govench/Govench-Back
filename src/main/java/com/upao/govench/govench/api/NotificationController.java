package com.upao.govench.govench.api;

import lombok.AllArgsConstructor;
import com.upao.govench.govench.model.dto.ReminderRequestDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;

    @PostMapping("/trigger-reminders")
    public ResponseEntity<String> triggerReminders(@RequestBody ReminderRequestDTO request) {
        User user = request.getUser();
        Event event = request.getEvent();
        notificationService.scheduleEventReminders(user, event);
        return ResponseEntity.ok("Reminders triggered");
    }
}