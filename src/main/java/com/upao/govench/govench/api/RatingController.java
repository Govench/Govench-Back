package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.RatingDTO;
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

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<RatingDTO>> getRatingsByEvent(@PathVariable int eventId) {
        List<RatingDTO> ratings = ratingService.getRatingsByEventId(eventId);
        if (ratings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
