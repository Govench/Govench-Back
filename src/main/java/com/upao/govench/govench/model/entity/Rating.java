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

    // Relación con el usuario que otorga la calificación
    @ManyToOne
    @JoinColumn(name = "rater_id", nullable = false)
    private User raterUser;

    // Relación con el usuario que recibe la calificación
    @ManyToOne
    @JoinColumn(name = "rated_id", nullable = false)
    private User ratedUser;

    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue; // Escala de 1 a 5, por ejemplo.

    @Column(name = "comment", length = 500)
    private String comment; // Comentario opcional del usuario que califica.
}

