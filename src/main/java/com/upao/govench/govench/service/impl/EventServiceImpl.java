package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.EventMapper;
import com.upao.govench.govench.mapper.LocationMapper;
import com.upao.govench.govench.mapper.RatingEventMapper;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.RatingEvent;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.RatingEventRepository;
import com.upao.govench.govench.service.EventService;
import com.upao.govench.govench.service.LocationService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RatingEventRepository ratingEventRepository;
    private final RatingEventMapper ratingEventMapper;
    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {

        return eventMapper.convertToListDTO(eventRepository.findAllUpcomingEvents(LocalDate.now()));
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

    @Transactional(readOnly = true)
    public EventResponseDTO findEventById(int id) {
        EventResponseDTO event = eventMapper.convertToDTO(getEventById(id));
        return event;
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        if(eventRequestDTO.getDate().isBefore(LocalDate.now()))
        {
            throw new IllegalArgumentException("No puedes crear un evento con fecha pasada");
        }
        LocalDateTime localDateStartTime = LocalDateTime.of(eventRequestDTO.getDate(),eventRequestDTO.getStartTime());
        LocalDateTime localDateEndTime = LocalDateTime.of(eventRequestDTO.getDate(),eventRequestDTO.getEndTime());
        if(localDateStartTime.isBefore(LocalDateTime.now()))
        {
            throw new IllegalArgumentException("No puedes crear un evento con una hora pasada");
        }
        if (localDateEndTime.isBefore(localDateStartTime)) {
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


        if(eventRequestDTO.getDate().isBefore(LocalDate.now()))
        {
            throw new IllegalArgumentException("No puedes crear un evento con fecha pasada");
        }
        LocalDateTime localDateStartTime = LocalDateTime.of(eventRequestDTO.getDate(),eventRequestDTO.getStartTime());
        LocalDateTime localDateEndTime = LocalDateTime.of(eventRequestDTO.getDate(),eventRequestDTO.getEndTime());
        if(localDateStartTime.isBefore(LocalDateTime.now()))
        {
            throw new IllegalArgumentException("No puedes crear un evento con una hora pasada");
        }
        if (localDateEndTime.isBefore(localDateStartTime)) {
            throw new IllegalArgumentException("La hora de fin del evento debe ser mayor que la del inicio");
        }
        if (Duration.between(eventRequestDTO.getStartTime(), eventRequestDTO.getEndTime()).toHours() < 2) {
            throw new IllegalArgumentException("El evento debe durar mínimo 2 horas");
        }
        if(eventRequestDTO.getMaxCapacity() <=0)
        {
            throw new IllegalArgumentException("No puedes crear un evento con una capacidad menor a 0");
        }

        if(eventRequestDTO.getTittle()!= null)event.setTittle(eventRequestDTO.getTittle());
        if(eventRequestDTO.getDescription()!= null)event.setDescription(eventRequestDTO.getDescription());
        if(eventRequestDTO.getDate()!= null)event.setDate(eventRequestDTO.getDate());
        if(eventRequestDTO.getStartTime()!= null)event.setStartTime(eventRequestDTO.getStartTime());
        if(eventRequestDTO.getEndTime()!= null)event.setEndTime(eventRequestDTO.getEndTime());
        if(eventRequestDTO.getType()!= null)event.setType(eventRequestDTO.getType());
        if(eventRequestDTO.getCost()!= null)event.setCost(eventRequestDTO.getCost());
        LocationRequestDTO location = new LocationRequestDTO();
        if(eventRequestDTO.getDepartment()!= null ) location.setDepartament(eventRequestDTO.getDepartment());
        if(eventRequestDTO.getAddress()!= null ) location.setAddress(eventRequestDTO.getAddress());
        if(eventRequestDTO.getProvince()!= null) location.setProvince(eventRequestDTO.getProvince());
        if(eventRequestDTO.getDistrict()!= null) location.setDistrict(eventRequestDTO.getDistrict());

        LocationResponseDTO locationResponseDTO =locationService.updateLocation(event.getLocation().getId(),location);
        event.setLocation(locationMapper.convertToEntity(locationResponseDTO));
        event = eventRepository.save(event);
        return eventMapper.convertToDTO(event);
    }

    public void deleteEvent(Integer id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un evento con el número de ID: " + id));


        if (event.getstatusdeleted()) {
            throw new IllegalArgumentException("No puedes eliminar un evento que ya está marcado como eliminado.");
        }


        event.setDeleted(true);
        eventRepository.save(event);
    }

    @Override
    public List<EventResponseDTO> findNotDeletedEvents() {
        return eventMapper.convertToListDTO(eventRepository.findByDeletedFalse());
    }

    @Transactional(readOnly = true)
    public List<RatingEventResponseDTO> getRatingEvents(Event eventId) {
        List<RatingEvent> ratingEvents = ratingEventRepository.findRatingEventByEventId(eventId);
        return ratingEventMapper.convertToListDTO(ratingEvents);
    }

    public List<EventResponseDTO> getEventobyUser (){
        // Obtener todos los eventos creados por el usuario
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        List<Event> events = eventRepository.findByOwner_Id(userId);
        return eventMapper.convertToListDTO(events);
    }
}
