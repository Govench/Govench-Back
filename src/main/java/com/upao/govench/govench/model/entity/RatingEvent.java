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


    @Column(name = "use_id_in", nullable = false)
    private int userId;

    @Column(name = "eve_id_in", nullable = false)
    private int eventId;

}
