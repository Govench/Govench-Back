package com.upao.govench.govench.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class CollectionEventPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "eve_id_in"
            , foreignKey = @ForeignKey(name = "FK_collectionsEvents_events"))
    private Event event;

    @ManyToOne
    @JoinColumn(name = "collection_id", referencedColumnName = "id"
            , foreignKey = @ForeignKey(name = "FK_collectionsEvents_collections"))
    private Collection collection;
}
