package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.Integration.email.dto.Mail;
import com.upao.govench.govench.Integration.email.entity.EmailService;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.service.RegisterConfirmation;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class RegisterConfirmationImpl implements RegisterConfirmation {

    private final EmailService emailService;

    public void sendReservationEmailToUser(User user, Event event) throws MessagingException {
        Map<String, Object> model = new HashMap<>();
        model.put("userName", user.getOrganizer() != null ? user.getOrganizer().getName() : user.getParticipant().getName());
        model.put("eventDetails", generateEventDetails(event));
        Mail mail = emailService.createMail(
                user.getEmail(),
                "CONFIRMACIÓN DE REGISTRO A EVENTO",
                model
        );

        emailService.sendEmail(mail, "email/user-confirmation-template");
    }

    public void sendReservationEmailToOwner(User user, Event event) throws MessagingException {
        String ownerName = event.getOwner().getOrganizer() != null
                ? event.getOwner().getOrganizer().getName() + " " + event.getOwner().getOrganizer().getLastname()
                : "Usuario";

        String participantName = user.getOrganizer() != null
                ? user.getOrganizer().getName() + " " + user.getOrganizer().getLastname()
                : user.getParticipant().getName() + " " + user.getParticipant().getLastname();

        Map<String, Object> model = new HashMap<>();
        model.put("ownerName", ownerName);
        model.put("participantName", participantName);
        model.put("participantEmail", user.getEmail());
        model.put("eventDetails", generateEventDetails(event));

        Mail mail = emailService.createMail(
                event.getOwner().getEmail(),
                "UN USUARIO SE HA INSCRITO A TU EVENTO",
                model
        );

        emailService.sendEmail(mail, "email/owner-notification-template");
    }

    private Map<String, String> generateEventDetails(Event event) {
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
