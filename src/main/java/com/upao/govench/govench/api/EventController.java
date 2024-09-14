package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Location;
import com.upao.govench.govench.repository.LocationRepository;
import com.upao.govench.govench.service.impl.EventService;
import com.upao.govench.govench.service.impl.LocationService;
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

    //@GetMapping("/{id}")
    //public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Integer id) {
    //    EventResponseDTO event = eventService.getEventById(id);
    //    return new ResponseEntity<>(event, HttpStatus.OK);
    //}

    @GetMapping("/{tittle}")
    public ResponseEntity<EventResponseDTO> getEventByTittle(@PathVariable String tittle) {
        EventResponseDTO event = eventService.getEventByName(tittle);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO createdEvent = eventService.createEvent(eventRequestDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Integer id,
                                                        @RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventRequestDTO);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

