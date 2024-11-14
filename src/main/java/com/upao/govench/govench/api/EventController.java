package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController{

    private final EventService eventService;


    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    @GetMapping("/tittle/{tittle}")
    public ResponseEntity<?> getEventByTittle(@PathVariable String tittle) {

            List<EventResponseDTO> event = eventService.getEventByName(tittle);
        if (event.isEmpty()) {
            return new ResponseEntity<>("Evento No Encontrado",HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Integer id) {
        EventResponseDTO event = eventService.findEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping("/exp/{exp}")
    public ResponseEntity<List<EventResponseDTO>> getEventByExp(@PathVariable String exp) {
       List<EventResponseDTO> event = eventService.getEventByExp(exp);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/ratings")
    public ResponseEntity<List<RatingEventResponseDTO>> getRatingEvents(@PathVariable Event eventId) {
        List<RatingEventResponseDTO> ratingEvents = eventService.getRatingEvents(eventId);

        return new ResponseEntity<>(ratingEvents, HttpStatus.OK);
    }
    @GetMapping("/myEvents")
    public ResponseEntity<List<EventResponseDTO>> getMyEvents() {
        List<EventResponseDTO> myEvents = eventService.getEventobyUser();
        return new ResponseEntity<>(myEvents, HttpStatus.OK);
    }
}