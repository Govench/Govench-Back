package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "User_Rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rater_user_id", nullable = false)
    private Participant raterUser; // Usuario que está realizando la calificación

    @ManyToOne
    @JoinColumn(name = "rated_user_id", nullable = false)
    private Participant ratedUser; // Usuario que está siendo calificado

    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue; // Escala de 1 a 5, por ejemplo.

    @Column(name = "comment", length = 500)
    private String comment; // Comentario opcional del usuario que califica.
}

