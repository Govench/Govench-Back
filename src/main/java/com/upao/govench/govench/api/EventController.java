package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.service.impl.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        EventResponseDTO event = eventService.getEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    //Hace conflicto con el GetMapping de arriba
    //@GetMapping("/{tittle}")
    //public ResponseEntity<EventResponseDTO> getEventByTittle(@PathVariable String tittle) {
    //    EventResponseDTO event = eventService.getEventByName(tittle);
    //    return new ResponseEntity<>(event, HttpStatus.OK);
    //}

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO createdEvent = eventService.createEvent(eventRequestDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id,
                                                        @RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventRequestDTO);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

