package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rating_event")
public class RatingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratingEvent_id")
    private int id;

    @Column(name = "rat_val_in", nullable = false)
    private int valorPuntuacion;

    @Column(name = "rat_fech_va", nullable = false)
    private LocalDate fechaPuntuacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useId", referencedColumnName = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", referencedColumnName = "eve_id_in")
    private Event eventId;

}
