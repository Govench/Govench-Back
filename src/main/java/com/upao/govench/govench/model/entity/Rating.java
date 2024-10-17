package com.upao.govench.govench.model.entity;


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



    // Relación con el organizador que otorga la calificación
    @ManyToOne
    @JoinColumn(name = "rater_organizer_id")
    private Organizer raterOrganizer;

    // Relación con el participante que otorga la calificación
    @ManyToOne
    @JoinColumn(name = "rater_participant_id")
    private Participant raterParticipant;

    // Relación con el organizador que recibe la calificación
    @ManyToOne
    @JoinColumn(name = "rated_organizer_id")
    private Organizer ratedOrganizer;

    // Relación con el participante que recibe la calificación
    @ManyToOne
    @JoinColumn(name = "rated_participant_id")
    private Participant ratedParticipant;

    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue; // Escala de 1 a 5, por ejemplo.

    @Column(name = "comment", length = 500)
    private String comment; // Comentario opcional del usuario que califica.
}

