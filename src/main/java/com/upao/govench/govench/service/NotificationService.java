package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.UserEvent;
import jakarta.mail.MessagingException;

public interface NotificationService {
    void sendDailyReminder(User user, Event event) throws MessagingException;
    void sendSameDayReminder(User user, Event event, int hoursBeforeEvent) throws MessagingException;
    void sendFinalReminder(User user, Event event) throws MessagingException;
    void scheduleEventReminders(UserEvent userEvent);
}
