package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import jakarta.mail.MessagingException;

public interface RegisterConfirmation {
   void sendReservationEmailToUser(User user, Event event) throws MessagingException;
    void sendReservationEmailToOwner(User user, Event event) throws MessagingException;
}
