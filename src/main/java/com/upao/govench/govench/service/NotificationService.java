package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.UserEvent;

public interface NotificationService {
    void sendDailyReminder(User user, Event event);
    void sendSameDayReminder(User user, Event event, int hoursBeforeEvent);
    void sendFinalReminder(User user, Event event);
    void scheduleEventReminders(UserEvent userEvent);
}
