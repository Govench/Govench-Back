package com.upao.govench.govench.model.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id_in", nullable = false)
    private int id;

    @Column(name = "post_body_vc", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "post_autor_vc", nullable = false)
    private User autor;

    @ManyToOne
    @JoinColumn(name = "post_comunidad_id", nullable = false)
    private Community comunidad;

    @Column(name = "post_created_dt", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date created;
}

