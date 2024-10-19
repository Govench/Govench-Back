package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.UserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrganizerEventServiceImpl {
    @Autowired
    private UserEventRepository userEventRepository;

    public List<User> getParticipantsByEvent(int id_events) {
        List<User> participants = userEventRepository.findUsersByEventId(id_events);
        return participants;


    }
}
