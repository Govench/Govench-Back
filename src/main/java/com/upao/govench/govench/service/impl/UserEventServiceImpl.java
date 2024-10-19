package com.upao.govench.govench.service.impl;
import com.upao.govench.govench.mapper.EventMapper;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.UserProfileDTO;
import com.upao.govench.govench.model.dto.UserRegistrationDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.UserEventRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEventServiceImpl implements UserEventService {
    @Autowired
    private final UserEventRepository userEventRepository;
    private final EventMapper eventMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;


    public UserProfileDTO registerParticipant(UserRegistrationDTO userRegistrationDTO) {
        return null;
    }

    /*metodos pre security*/

    @Override
    public UserEvent addUserEvent(UserEvent userEvent) {

        User user = userRepository.findById(userEvent.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        if ("ROLE_ADMIN".equals(user.getRole().getName())) {
            throw new RuntimeException("Solo participantes y organizadores pueden registrarse a un evento");
        }

        Event event = eventRepository.findById(userEvent.getEvent().getId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (event.getRegisteredCount() >= event.getMaxCapacity()) {
            throw new RuntimeException("No hay cupos disponibles para este evento");
        }

        // Verificar si el usuario ya está registrado en el evento
        Optional<UserEvent> existingRegistration = userEventRepository.findById(userEvent.getId());
        if (existingRegistration.isPresent()) {
            throw new RuntimeException("El usuario ya está registrado en este evento");
        }

        event.setRegisteredCount(event.getRegisteredCount() + 1);
        eventRepository.save(event);

        userEvent.setUser(user);
        userEvent.setEvent(event);
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



    // Constructor manual para la inyección de dependencias
    public UserEventServiceImpl(UserEventRepository userEventRepository, EventMapper eventMapper) {
        this.userEventRepository = userEventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventResponseDTO> getEventHistory(Integer userId) {
        List<UserEvent> userEvents = userEventRepository.findAllByUserId(userId);
        return userEvents.stream()
                .map(userEvent -> eventMapper.convertToDTO(userEvent.getEvent())) // Usamos el método existente
                .toList();
    }

}
