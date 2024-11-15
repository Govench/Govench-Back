package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.service.EmailService;
import com.upao.govench.govench.service.NotificationService;
import com.upao.govench.govench.service.RegisterConfirmation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterConfirmationImpl implements RegisterConfirmation {
    @Autowired
    private EmailService emailService;

    @Autowired
    private  NotificationServiceImpl notificationServiceImpl;

    public void sendReservationEmailToUser(User user, Event event) {
        String subject = "\u2705 CONFIRMACIÓN DE REGISTRO A EVENTO";
        String body = createEmailBodyToUser(user, event) + notificationServiceImpl.generateSignature();
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String createEmailBodyToUser(User user, Event event) {
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("Hola, ").append(user.getEmail()).append(",\n\n")
                .append("Su registro al evento " + event.getTittle() + " ha sido confirmado.\n\n")
                .append("Detalles de su registro al evento:\n")
                .append("Descripción: ").append(event.getDescription()).append("\n")
                .append(notificationServiceImpl.generateEventDetails(event)).append("\n")
                .append("\uD83D\uDCC5 Fecha: ").append(event.getDate()).append("\n")
                .append("\uD83D\uDD50 Hora de Inicio: ").append(event.getStartTime()).append("\n")
                .append("\uD83D\uDD50 Hora de Fin: ").append(event.getEndTime()).append("\n\n\n");

        return emailBody.toString();
    }

    public void sendReservationEmailToOwner(User user, Event event) {
        String emailOwner = event.getOwner().getEmail();

        String subject = "\u2705 UN USUARIO SE HA INSCRITO A TU EVENTO";
        String body = createEmailBodyToOwner(user, event) + notificationServiceImpl.generateSignature();

        emailService.sendEmail(emailOwner, subject, body);
    }

    private String createEmailBodyToOwner(User user, Event event) {
        StringBuilder emailBody = new StringBuilder();
        String nameParticipant="";
        String lastNameParticipant="";
        String nameOwner="";
        String lastNameOwner="";
        if(event.getOwner().getParticipant()!=null)
        {

        }
        if(event.getOwner().getOrganizer()!=null)
        {
            nameOwner = event.getOwner().getOrganizer().getName();
            lastNameOwner = event.getOwner().getOrganizer().getLastname();
        }

        if(user.getOrganizer()!=null)
        {
            nameParticipant = user.getOrganizer().getName();
            lastNameParticipant = user.getOrganizer().getLastname();
        }
        if(user.getParticipant()!=null)
        {
            nameParticipant = user.getParticipant().getName();
            lastNameParticipant = user.getParticipant().getLastname();
        }

        String emailParticipant = user.getEmail();

        emailBody.append("Hola, ").append(nameOwner+" "+lastNameOwner+"\n\n")
                .append("El usuario: "+nameParticipant+" "+lastNameParticipant+"\n")
                .append("Con correo: "+emailParticipant+", se ha registrado a su siguiente evento organizador por usted:\n\n")
                .append(notificationServiceImpl.generateEventDetails(event)+"\n");

        return emailBody.toString();
    }

}
