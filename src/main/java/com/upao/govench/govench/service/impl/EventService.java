package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.EventMapper;
import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private EventRepository eventRepository;
    private EventMapper eventMapper;

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.convertToListDTO(events);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Evento no encontrado con el numero de ID"+id));
        return eventMapper.convertToDTO(event);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventByName(String tittle) {
        Event event = eventRepository.findAllByEventTittle(tittle)
                .orElseThrow(()->new ResourceNotFoundException("Evento no encontrado con el titulo "+tittle));
        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = eventMapper.convertToEntity(eventRequestDTO);
        eventRepository.save(event);
        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Evento no encontrado con el numero de ID"+id));
        event.setTittle(eventRequestDTO.getTittle());
        event.setDescription(eventRequestDTO.getDescription());
        event.setDate(eventRequestDTO.getDate());
        event.setStartTime(eventRequestDTO.getStartDate());
        event.setEndTime(eventRequestDTO.getEndDate());
        event.setState(eventRequestDTO.getState());
        event.setType(eventRequestDTO.getType());
        event.setCost(eventRequestDTO.getCost());

        event = eventRepository.save(event);

        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
