package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "collections_events")
@IdClass(CollectionEventPK.class)
public class CollectionEvent {

    @Id
    private Integer event;

    @Id
    private Integer collection;

    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate;

}
