package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.EventMapper;
import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.Location;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
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
        event.setTittle(eventRequestDTO.getTittle());
        event.setDescription(eventRequestDTO.getDescription());
        event.setDate(eventRequestDTO.getDate());
        event.setStartTime(eventRequestDTO.getStartTime());
        event.setEndTime(eventRequestDTO.getEndTime());
        event.setState(eventRequestDTO.getState());
        event.setType(eventRequestDTO.getType());
        event.setCost(eventRequestDTO.getCost());
        event.setLocation(eventRequestDTO.getLocation());

        event = eventRepository.save(event);

        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }
}
