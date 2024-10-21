package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.CollectionEvent;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.service.CollectionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/collections-events")
public class CollectionEventController {

    private final CollectionEventService collectionEventService;

    @PostMapping("{collectionId}/add-event")
    public ResponseEntity<CollectionEvent> addEventToCollection(@PathVariable Integer collectionId,
                                                              @RequestParam Integer eventId){
        CollectionEvent collectionEvent = collectionEventService.addCollectionEvent(eventId, collectionId);
        return new ResponseEntity<>(collectionEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{collectionId}/events")
    public ResponseEntity<List<Event>> getEventsInCollection(@PathVariable Integer collectionId){
        List<Event> events = collectionEventService.getEventsInCollection(collectionId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @DeleteMapping("{collectionId}/remove-event/{eventId}")
    public ResponseEntity<Void> removeEventFromCollection(@PathVariable Integer eventId,
                                                          @PathVariable Integer collectionId){
        collectionEventService.deleteCollectionEvent(eventId, collectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
