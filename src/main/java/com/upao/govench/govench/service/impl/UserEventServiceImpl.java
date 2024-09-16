package com.upao.govench.govench.service.impl;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserEventServiceImpl implements UserEventService {
    @Autowired
    private UserEventRepository userEventRepository;

    @Override
    public UserEvent addUserEvent(UserEvent userEvent) {
        return userEventRepository.save(userEvent);
    }

    @Override
    public UserEvent searchUserEventById(IdCompuestoU_E id) {
        return userEventRepository.findById(id).orElse(null);
    }

    @Override
    public void removeUserEventById(IdCompuestoU_E id) {
        if (userEventRepository.existsById(id))
        {
            userEventRepository.deleteById(id);
        }
    }

    @Override
    public List<UserEvent> getUserEventbyUser(User user) {
       return userEventRepository.findByUser(user);
    }

    @Override
    public List<UserEvent> getUserEventbyEvent(Event event) {
        return userEventRepository.findByEvent(event);
    }

    @Override
    public List<UserEvent> getAllUserEvents() {
        return userEventRepository.findAll();
    }

}
