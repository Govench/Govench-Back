package com.upao.govench.govench.api;

import com.upao.govench.govench.mapper.UserEventMapper;
import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.service.EventService;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController{

    private final EventServiceImpl eventServiceImpl;


    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventServiceImpl.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    @GetMapping("/tittle/{tittle}")
    public ResponseEntity<?> getEventByTittle(@PathVariable String tittle) {

            List<EventResponseDTO> event = eventServiceImpl.getEventByName(tittle);
        if (event.isEmpty()) {
            return new ResponseEntity<>("Evento No Encontrado",HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
    }

    @GetMapping("/exp/{exp}")
    public ResponseEntity<List<EventResponseDTO>> getEventByExp(@PathVariable String exp) {
       List<EventResponseDTO> event = eventServiceImpl.getEventByExp(exp);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/ratings")
    public ResponseEntity<List<RatingEventResponseDTO>> getRatingEvents(@PathVariable Event eventId) {
        List<RatingEventResponseDTO> ratingEvents = eventServiceImpl.getRatingEvents(eventId);

        return new ResponseEntity<>(ratingEvents, HttpStatus.OK);
    }
}