package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.OrganizerRepository;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.service.NotificationService;
import com.upao.govench.govench.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final UserEventRepository userEventRepository;

    @Override
    public void sendDailyReminder(User user, Event event) {
        String subject = "\uD83D\uDD14 RECORDATORIO PARA EL EVENTO: " + event.getTittle();
        String message = buildPersonalizedMessage(user, event, "\uD83D\uDCE8 Tienes un próximo evento al que te has inscrito.");
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Override
    public void sendSameDayReminder(User user, Event event, int hoursBeforeEvent) {
        String subject = "\uD83D\uDD14 RECORDATORIO PARA EL EVENTO DE HOY!";
        String message = buildPersonalizedMessage(user, event, "\uD83D\uDCE8 ¡Recuerda! El evento comenzará en " + hoursBeforeEvent + " horas.");
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Override
    public void sendFinalReminder(User user, Event event) {
        String subject = "\uD83D\uDD14 ¡ÚLTIMO RECORDATORIO! EL EVENTO ESTÁ POR COMENZAR";
        String message = buildPersonalizedMessage(user, event, "\uD83D\uDCE8 El evento está a punto de comenzar. ¡Te esperamos!");
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    @Scheduled(cron = "*/10 * * * * ?") // Ejecuta el metodo de manera automatica cada 30 seg
    public void sendReminders() {
        LocalDate now = LocalDate.now();
        List<UserEvent> usersWithNotifications = userEventRepository.findUsersWithNotificationsEnabled(); // Obtiene aquellos con la notificacion activada...

        for (UserEvent userEvent : usersWithNotifications) { // y de ellos, recorre cada usuario y su evento asociado...
            Event event = userEvent.getEvent();

            if (event.getDate().isAfter(now) || event.getDate().isEqual(now)) { // en caso el evento sea hoy o en el futuro...,
                scheduleEventReminders(userEvent); // activa el envio de recordatorio sobre ese evento.
            }
        }
    }

    public void scheduleEventReminders(UserEvent userEvent) {
        User user = userEvent.getUser();
        Event event = userEvent.getEvent();
        LocalDate today = LocalDate.now();
        LocalDate eventDate = event.getDate();

        long daysUntilEvent = ChronoUnit.DAYS.between(today, eventDate); // Calcula los días hasta el evento.

        // Envía recordatorios diarios cuando el evento es en 3, 2 o 1 días, y si no se ha enviado un recordatorio hoy
        if ((daysUntilEvent == 3 || daysUntilEvent == 2 || daysUntilEvent == 1)
                && !today.equals(userEvent.getLastReminderSentDate())) {
            sendDailyReminder(user, event);
            userEvent.setLastReminderSentDate(today); // Registra la fecha del último recordatorio.
            userEventRepository.save(userEvent); // Guarda el evento con la fecha actualizada del último recordatorio.
        } else if (daysUntilEvent == 0) { // Si el evento es hoy, decide entre un recordatorio del mismo día o final.
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());

            long hoursUntilEvent = ChronoUnit.HOURS.between(now, eventDateTime); // Calcula las horas restantes hasta el evento
            if (hoursUntilEvent >= 2 && hoursUntilEvent <= 6 && !userEvent.isSameDayReminderSent()) {
                sendSameDayReminder(user, event, (int) hoursUntilEvent);  // Envía el recordatorio del mismo día.
                userEvent.setSameDayReminderSent(true); // Registra que se ha enviado este recordatorio.
                userEvent.setLastReminderSentDate(today); // Registra la fecha del último recordatorio.
                userEventRepository.save(userEvent); // Guarda el evento actualizado
            } else if (hoursUntilEvent < 2 && !userEvent.isFinalReminderSent()) { // Si faltan menos de 2 horas y no se ha enviado un recordatorio final:
                sendFinalReminder(user, event);
                userEvent.setFinalReminderSent(true); // Registra el envio del recordatorio
                userEvent.setLastReminderSentDate(today); // Registra la fecha del último recordatorio.
                userEventRepository.save(userEvent); // Guarda
            }
        }
    }

    private String buildPersonalizedMessage(User user, Event event, String reminderText) {
        String greeting = generateGreeting(user);
        String eventDetails = generateEventDetails(event);
        String signature = generateSignature();

        // Combine greeting, reminder, and event details
        return String.format("%s,\n\n%s\n\n%s\n\n\n%s",
                greeting, reminderText, eventDetails, signature);
    }

    private String generateGreeting(User user) {
        if (user.getOrganizer() != null && user.getOrganizer().getName() != null) {
            return "\uD83D\uDC4B Hola " + user.getOrganizer().getName() + "!";
        } else if (user.getParticipant() != null && user.getParticipant().getName() != null) {
            return "\uD83D\uDC4B Hola " + user.getParticipant().getName() + "!";
        } else {
            return "\uD83D\uDC4B Hola Administrador!";
        }
    }

    public String generateEventDetails(Event event) {
        String location = (event.getLocation() != null) // Verifica si el evento tiene una ubicación asignada.
                ? String.format("%s, %s, %s",  // Si es así, asigna una cadena formateada con la ubicación.
                event.getLocation().getDepartament(),
                event.getLocation().getProvince(),
                event.getLocation().getDistrict())
                : "Ubicación no disponible";

        return "Detalles del Evento:\n" +
                "\uD83C\uDF7E Título: " + event.getTittle() + "\n" +
                "\uD83D\uDCC5 Fecha: " + event.getDate() + "\n" +
                "\uD83D\uDD50 Hora: " + event.getStartTime() + "\n" +
                "\uD83C\uDFE6 Lugar: " + location + "\n" +
                "\uD83D\uDCDD Descripción: " + event.getDescription();
    }
    public String generateSignature() {
        return "Atentamente,\n" +
                " Govench Team\n" +
                "\uD83C\uDF10 www.govench.com\n" +
                "\uD83D\uDCDE +51 990 099 990\n" +
                "\uD83D\uDCE7 govench6@gmail.com";
    }
}
