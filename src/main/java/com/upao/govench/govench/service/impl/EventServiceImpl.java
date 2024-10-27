package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.EventMapper;
import com.upao.govench.govench.mapper.LocationMapper;
import com.upao.govench.govench.mapper.RatingEventMapper;
import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.RatingEvent;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.LocationRepository;
import com.upao.govench.govench.repository.RatingEventRepository;
import com.upao.govench.govench.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RatingEventRepository ratingEventRepository;
    private final RatingEventMapper ratingEventMapper;
    private final LocationRepository locationRepository;
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
        if(eventRequestDTO.getDate().isBefore(LocalDate.now()))
        {
            throw new IllegalArgumentException("No puedes crear un evento con fecha pasada");
        }
        if(eventRequestDTO.getStartTime().isBefore(LocalTime.now()))
        {
            throw new IllegalArgumentException("No puedes crear un evento con una hora pasada");
        }
        if (eventRequestDTO.getEndTime().isBefore(eventRequestDTO.getStartTime())) {
            throw new IllegalArgumentException("La hora de fin del evento debe ser mayor que la del inicio");
        }
        if (Duration.between(eventRequestDTO.getStartTime(), eventRequestDTO.getEndTime()).toHours() < 2) {
            throw new IllegalArgumentException("El evento debe durar mínimo 2 horas");
        }
        if(eventRequestDTO.getMaxCapacity() <=0)
        {
            throw new IllegalArgumentException("No puedes crear un evento con una capacidad menor a 0");
        }
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
        if(eventRequestDTO.getType()!= null)event.setType(eventRequestDTO.getType());
        if(eventRequestDTO.getCost()!= null)event.setCost(eventRequestDTO.getCost());
        if(eventRequestDTO.getLocation()!= null)event.setLocation(locationRepository.findById(eventRequestDTO.getLocation()).orElse(null));

        event = eventRepository.save(event);

        return eventMapper.convertToDTO(event);
    }

    @Transactional
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RatingEventResponseDTO> getRatingEvents(Event eventId) {
        List<RatingEvent> ratingEvents = ratingEventRepository.findRatingEventByEventId(eventId);
        return ratingEventMapper.convertToListDTO(ratingEvents);
    }

}
