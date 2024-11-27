package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.Integration.email.dto.Mail;
import com.upao.govench.govench.Integration.email.entity.EmailService;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final UserEventRepository userEventRepository;

    @Override
    public void sendDailyReminder(User user, Event event) throws MessagingException {
        Map<String, Object> model = new HashMap<>();
        model.put("title", event.getTittle());
        model.put("userName", generateUserName(user));
        model.put("reminderText", "Tienes un próximo evento al que te has inscrito.");
        model.put("eventDetails", generateEventDetailsMap(event));

        Mail mail = emailService.createMail(
                user.getEmail(),
                "RECORDATORIO PARA EL EVENTO: " + event.getTittle(),
                model
        );

        emailService.sendEmail(mail, "email/reminder-template");
    }

    @Override
    public void sendSameDayReminder(User user, Event event, int hoursBeforeEvent) throws MessagingException {
        Map<String, Object> model = new HashMap<>();
        model.put("title", event.getTittle());
        model.put("userName", generateUserName(user));
        model.put("reminderText", "¡Recuerda! El evento comenzará en " + hoursBeforeEvent + " horas.");
        model.put("eventDetails", generateEventDetailsMap(event));

        Mail mail = emailService.createMail(
                user.getEmail(),
                "RECORDATORIO PARA EL EVENTO DE HOY!",
                model
        );

        emailService.sendEmail(mail, "email/reminder-template");
    }

    @Override
    public void sendFinalReminder(User user, Event event) throws MessagingException {
        Map<String, Object> model = new HashMap<>();
        model.put("title", event.getTittle());
        model.put("userName", generateUserName(user));
        model.put("reminderText", "El evento está a punto de comenzar. ¡Te esperamos!");
        model.put("eventDetails", generateEventDetailsMap(event));

        Mail mail = emailService.createMail(
                user.getEmail(),
                "¡ÚLTIMO RECORDATORIO! EL EVENTO ESTÁ POR COMENZAR",
                model
        );

        emailService.sendEmail(mail, "email/reminder-template");
    }

    @Scheduled(cron = "0 */5 * * * ?") // Ejecuta el método automáticamente cada 5 minutos
    public void sendReminders() {
        LocalDate today = LocalDate.now();
        List<UserEvent> usersWithNotifications = userEventRepository.findUsersWithNotificationsEnabled(); // Usuarios con notificaciones activadas.

        for (UserEvent userEvent : usersWithNotifications) { // Recorre usuarios y sus eventos asociados.
            Event event = userEvent.getEvent();

            if (event.getDate().isAfter(today) || event.getDate().isEqual(today)) { // Si el evento es hoy o en el futuro...
                scheduleEventReminders(userEvent); // Activa el envío de recordatorios.
            }
        }
    }

    public void scheduleEventReminders(UserEvent userEvent) {
        User user = userEvent.getUser();
        Event event = userEvent.getEvent();
        LocalDate today = LocalDate.now();
        LocalDate eventDate = event.getDate();

        long daysUntilEvent = ChronoUnit.DAYS.between(today, eventDate); // Calcula los días hasta el evento.

        // Envía recordatorios diarios para eventos en 3, 2 o 1 días si no se ha enviado uno hoy
        if ((daysUntilEvent == 3 || daysUntilEvent == 2 || daysUntilEvent == 1)
                && !today.equals(userEvent.getLastReminderSentDate())) {
            try {
                sendDailyReminder(user, event);
                userEvent.setLastReminderSentDate(today); // Registra la fecha del último recordatorio.
                userEventRepository.save(userEvent); // Guarda el evento con la fecha actualizada del último recordatorio.
            } catch (MessagingException e) {
                System.err.println("Error enviando recordatorio diario: " + e.getMessage());
            }
        } else if (daysUntilEvent == 0) { // Si el evento es hoy, decide entre un recordatorio del mismo día o final.
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());

            long hoursUntilEvent = ChronoUnit.HOURS.between(now, eventDateTime); // Calcula las horas restantes
            if (hoursUntilEvent >= 2 && hoursUntilEvent <= 6 && !userEvent.isSameDayReminderSent()) {
                try {
                    sendSameDayReminder(user, event, (int) hoursUntilEvent); // Envía el recordatorio del mismo día.
                    userEvent.setSameDayReminderSent(true); // Registra que se ha enviado este recordatorio.
                    userEvent.setLastReminderSentDate(today); // Registra la fecha del último recordatorio.
                    userEventRepository.save(userEvent); // Guarda el evento actualizado
                } catch (MessagingException e) {
                    System.err.println("Error enviando recordatorio del mismo día: " + e.getMessage());
                }
            } else if (hoursUntilEvent < 2 && !userEvent.isFinalReminderSent()) { // Si faltan menos de 2 horas y no se ha enviado un recordatorio final:
                try {
                    sendFinalReminder(user, event);
                    userEvent.setFinalReminderSent(true); // Registra el envío del recordatorio
                    userEvent.setLastReminderSentDate(today); // Registra la fecha del último recordatorio.
                    userEventRepository.save(userEvent); // Guarda
                } catch (MessagingException e) {
                    System.err.println("Error enviando recordatorio final: " + e.getMessage());
                }
            }
        }
    }

    private String generateUserName(User user) {
        if (user.getOrganizer() != null && user.getOrganizer().getName() != null) {
            return user.getOrganizer().getName();
        } else if (user.getParticipant() != null && user.getParticipant().getName() != null) {
            return user.getParticipant().getName();
        } else {
            return "Administrador";
        }
    }

    private Map<String, String> generateEventDetailsMap(Event event) {
        Map<String, String> eventDetails = new HashMap<>();
        String location = (event.getLocation() != null)
                ? String.format("%s, %s, %s",
                event.getLocation().getDepartament(),
                event.getLocation().getProvince(),
                event.getLocation().getDistrict())
                : "Ubicación no disponible";

        eventDetails.put("title", event.getTittle());
        eventDetails.put("date", event.getDate().toString());
        eventDetails.put("time", event.getStartTime().toString());
        eventDetails.put("location", location);
        eventDetails.put("description", event.getDescription());
        return eventDetails;
    }
}
