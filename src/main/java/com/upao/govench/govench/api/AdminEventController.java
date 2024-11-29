package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.service.EventService;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
public class AdminEventController {

    private final EventService eventServiceImpl;

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
    @GetMapping("/NotDeleted")
    public List<EventResponseDTO> getNotDeletedEvents() {
       return eventServiceImpl.findNotDeletedEvents();
    }
}
