package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;

public interface RegisterConfirmation {
    void sendReservationEmailToUser(User user, Event event);
    void sendReservationEmailToOwner(User user, Event event);
}
