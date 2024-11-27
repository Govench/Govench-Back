package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.Event;

import java.util.List;

public interface EventService {
    public List<EventResponseDTO> getAllEvents();
    public List<EventResponseDTO> getEventByName(String tittle);
    public List<EventResponseDTO> getEventByExp(String exp);
    public Event getEventById(int id);
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO);
    public EventResponseDTO updateEvent(Integer id, EventRequestDTO eventRequestDTO);
    public void deleteEvent(Integer id);
    public List<RatingEventResponseDTO> getRatingEvents(Event eventId);
    public List<EventResponseDTO> getEventobyUser ();
    public EventResponseDTO findEventById(int id);
    public List<EventResponseDTO> findNotDeletedEvents();
}
