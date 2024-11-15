package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.CollectionEvent;
import com.upao.govench.govench.model.entity.Event;

import java.util.List;

public interface CollectionEventService {
    CollectionEvent addCollectionEvent(Integer eventId, Integer collectionId);
    List<Event> getEventsInCollection(Integer collectionId);
    void deleteCollectionEvent(Integer eventId, Integer collectionId);
}
