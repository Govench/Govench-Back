package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.EventRepository;
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

    @Override
    public void sendDailyReminder(User user, Event event) {
        String subject = "Recordatorio Diario para el Evento " + event.getTittle();
        String message = "\n\nHola " + user.getName() + ", no olvides que tienes un evento próximo en " + event.getDate() + ".";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Override
    public void sendSameDayReminder(User user, Event event, int hoursBeforeEvent) {
        String subject = "Recordatorio del Evento Hoy!";
        String message = "Hola " + user.getName() + ", el evento '" + event.getTittle() + "' comenzará en " + hoursBeforeEvent + " horas.";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Override
    public void sendFinalReminder(User user, Event event) {
        String subject = "¡Último recordatorio! El evento está por comenzar";
        String message = "Hola " + user.getName() + ", el evento '" + event.getTittle() + "' comenzará en unos minutos.";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Scheduled(cron = "*/1 * * * * ?")
    @Override
    public void sendReminders() {
        LocalDate now = LocalDate.now();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Event> upcomingEvents = eventRepository.findUpcomingEventsForUser(user.getId(), now);
            for (Event event : upcomingEvents) {
                scheduleEventReminders(user, event);
            }
        }
    }

    public void scheduleEventReminders(User user, Event event) {
        LocalDate today = LocalDate.now(); // Solo la fecha actual
        LocalDate eventDate = event.getDate(); // Suponiendo que event.getDate() es LocalDate

        long daysUntilEvent = ChronoUnit.DAYS.between(today, eventDate);

        if (daysUntilEvent == 3 || daysUntilEvent == 2 || daysUntilEvent == 1) {
            sendDailyReminder(user, event);
        } else if (daysUntilEvent == 0) { // El mismo día del evento
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());

            long hoursUntilEvent = ChronoUnit.HOURS.between(now, eventDateTime);
            if (hoursUntilEvent >= 2 && hoursUntilEvent <= 6) {
                sendSameDayReminder(user, event, (int) hoursUntilEvent);
            } else if (hoursUntilEvent < 2) {
                sendFinalReminder(user, event);
            }
        }
    }
}