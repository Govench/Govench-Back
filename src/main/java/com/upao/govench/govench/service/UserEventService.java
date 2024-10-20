package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.UserProfileDTO;
import com.upao.govench.govench.model.dto.UserRegistrationDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.model.entity.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.upao.govench.govench.model.entity.User;
import org.springframework.stereotype.Service;

import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.UserService;



@Service
public interface UserEventService {

    public UserEvent addUserEvent(UserEvent userEvent);

    /*metodos pre security*/

    public UserEvent searchUserEventById(IdCompuestoU_E id);

    public void removeUserEventById(IdCompuestoU_E id);

    public List<UserEvent> getAllUserEvents();

    public List<UserEvent> getUserEventbyUser(User user);
    public  List<UserEvent> getUserEventbyEvent(Event event);

    List<EventResponseDTO> getEventHistory(Integer userId);

    public List<User> getParticipantsByEvent(int eventId);

}