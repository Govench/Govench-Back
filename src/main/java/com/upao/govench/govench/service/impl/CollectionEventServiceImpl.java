package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Collection;
import com.upao.govench.govench.model.entity.CollectionEvent;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CollectionEventRepository;
import com.upao.govench.govench.repository.CollectionRepository;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.service.CollectionEventService;
import com.upao.govench.govench.service.EventService;
import com.upao.govench.govench.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionEventServiceImpl implements CollectionEventService {

    private final CollectionEventRepository collectionEventRepository;
    private final CollectionRepository collectionRepository;

    private final UserService userService;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CollectionEvent addCollectionEvent(Integer eventId, Integer collectionId) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));

        if (collection.getUser().getId() != userId){
            throw new IllegalArgumentException("No eres dueño de la colección");
        }

        if (collectionEventRepository.existsByCollectionAndEvent(collection,event)) {
            throw new IllegalArgumentException("El evento ya existe en la colección"); // Inform user about existing event
        }

        LocalDateTime addedDate = LocalDateTime.now();
        collectionEventRepository.addEventToCollection(eventId, collectionId, addedDate);

        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setEvent(eventId);
        collectionEvent.setCollection(collectionId);
        collectionEvent.setAddedDate(addedDate);

        return collectionEvent;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> getEventsInCollection(Integer collectionId) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Colección no encontrada"));

        if (collection.getUser().getId() != userId){
            throw new IllegalArgumentException("No puedes vizualizar, No eres dueño de la colección");
        }

        return collectionEventRepository.findEventsByCollection(collection);
    }

    @Transactional
    @Override
    public void deleteCollectionEvent(Integer eventId, Integer collectionId) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));

        if (collection.getUser().getId() != userId){
            throw new IllegalArgumentException("No eres dueño de la colección");
        }

        collectionEventRepository.deleteByEventAndCollection(eventId, collectionId);
    }

}
