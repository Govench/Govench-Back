package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.model.dto.RatingDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.service.EventService;
import com.upao.govench.govench.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RatingController.java
@RestController
@RequestMapping("/ratings")
@PreAuthorize("hasAnyRole('PARTICIPANT', 'ORGANIZER')")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private EventService eventService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Object> getRatingsByEvent(@PathVariable int eventId) {
        try {
            // Verificar si el evento existe, esto lanzará una excepción si no lo encuentra
            Event event = eventService.getEventById(eventId);

            // Obtener las calificaciones del evento
            List<RatingDTO> ratings = ratingService.getRatingsByEventId(eventId);
            if (ratings.isEmpty()) {
                // Retornar un mensaje indicando que no hay calificaciones
                return new ResponseEntity<>("No hay calificaciones disponibles para este evento.", HttpStatus.OK);
            }

            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // Manejar la excepción lanzada por el servicio y retornar un mensaje personalizado
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




}
