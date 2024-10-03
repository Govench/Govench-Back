package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.EventMapper;
import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.LocationRepository;
import com.upao.govench.govench.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.convertToListDTO(events);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventByName(String tittle) {
      List<Event> event = eventRepository.findAllByEventTittle(tittle);
        return eventMapper.convertToListDTO(event);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventByExp(String exp) {
        List<Event> event = eventRepository.findAllByEventExp(exp);
         return eventMapper.convertToListDTO(event);
    }

    @Transactional(readOnly = true)
    public Event getEventById(int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Evento no encontrado "));
        return event;
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = eventMapper.convertToEntity(eventRequestDTO);
        eventRepository.save(event);
        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public EventResponseDTO updateEvent(Integer id, EventRequestDTO eventRequestDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Evento no encontrado con el numero de ID"+id));
        if(eventRequestDTO.getTittle()!= null)event.setTittle(eventRequestDTO.getTittle());
        if(eventRequestDTO.getDescription()!= null)event.setDescription(eventRequestDTO.getDescription());
        if(eventRequestDTO.getDate()!= null)event.setDate(eventRequestDTO.getDate());
        if(eventRequestDTO.getStartTime()!= null)event.setStartTime(eventRequestDTO.getStartTime());
        if(eventRequestDTO.getEndTime()!= null)event.setEndTime(eventRequestDTO.getEndTime());
        if(eventRequestDTO.getState()!= null)event.setState(eventRequestDTO.getState());
        if(eventRequestDTO.getType()!= null)event.setType(eventRequestDTO.getType());
        if(eventRequestDTO.getCost()!= null)event.setCost(eventRequestDTO.getCost());
        if(eventRequestDTO.getLocation()!= null)event.setLocation(eventRequestDTO.getLocation());

        event = eventRepository.save(event);

        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

}
