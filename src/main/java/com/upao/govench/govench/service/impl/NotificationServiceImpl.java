package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.NotificationService;
import com.upao.govench.govench.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;
    private final UserEventRepository userEventRepository;

    @Override
    public void sendDailyReminder(User user, Event event) {
        String subject = "Recordatorio Diario para el Evento " + event.getTittle();
        String message = "\n\nHola " + user.getParticipant().getName() + ", no olvides que tienes un evento próximo en " + event.getDate() + ".";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Override
    public void sendSameDayReminder(User user, Event event, int hoursBeforeEvent) {
        String subject = "Recordatorio del Evento Hoy!";
        String message = "Hola " + user.getParticipant().getName() + ", el evento '" + event.getTittle() + "' comenzará en " + hoursBeforeEvent + " horas.";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Override
    public void sendFinalReminder(User user, Event event) {
        String subject = "¡Último recordatorio! El evento está por comenzar";
        String message = "Hola " + user.getParticipant().getName() + ", el evento '" + event.getTittle() + "' comenzará en unos minutos.";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Scheduled(cron = "0 */10 * * * ?")
    @Override
    public void sendReminders() {
        LocalDate now = LocalDate.now();
        List<UserEvent> usersWithNotifications = userEventRepository.findUsersWithNotificationsEnabled();

        for (UserEvent userEvent : usersWithNotifications) {
            User user = userEvent.getUser();
            Event event = userEvent.getEvent();

            if (event.getDate().isAfter(now) || event.getDate().isEqual(now)) {
                scheduleEventReminders(user, event);
            }
        }
    }

    public void scheduleEventReminders(User user, Event event) {
        LocalDate today = LocalDate.now();
        LocalDate eventDate = event.getDate();

        long daysUntilEvent = ChronoUnit.DAYS.between(today, eventDate);

        if ((daysUntilEvent == 3 || daysUntilEvent == 2 || daysUntilEvent == 1) && !today.equals(event.getLastReminderSentDate())) {
            sendDailyReminder(user, event);
            event.setLastReminderSentDate(today);
            eventRepository.save(event);
        } else if (daysUntilEvent == 0) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());

            long hoursUntilEvent = ChronoUnit.HOURS.between(now, eventDateTime);
            if (hoursUntilEvent >= 2 && hoursUntilEvent <= 6 && !event.isSameDayReminderSent()) {
                sendSameDayReminder(user, event, (int) hoursUntilEvent);
                event.setSameDayReminderSent(true);
                eventRepository.save(event);
            } else if (hoursUntilEvent < 2 && !event.isFinalReminderSent()) {
                sendFinalReminder(user, event);
                event.setFinalReminderSent(true);
                eventRepository.save(event);
            }
        }
    }
}
