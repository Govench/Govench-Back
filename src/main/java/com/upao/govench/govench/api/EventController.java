package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

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

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO createdEvent = eventServiceImpl.createEvent(eventRequestDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Integer id,
                                                        @RequestBody EventRequestDTO eventRequestDTO) {
        EventResponseDTO updatedEvent = eventServiceImpl.updateEvent(id, eventRequestDTO);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventServiceImpl.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

