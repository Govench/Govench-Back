package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pos_id_in", nullable = false)
    private int id;

    @Column(name = "pos_bod_vc", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "pos_aut_vc", nullable = false,referencedColumnName = "user_id")
    private User autor;

    @ManyToOne
    @JoinColumn(name = "pos_com_id", nullable = false)
    private Community comunidad;

    @Column(name = "post_crd_dt", nullable = false)
    private LocalDate created;

    @Column(name = "post_upd_dt", nullable = true)
    private LocalDateTime updated;

    @PrePersist
    public void prePersist() {
        this.created = LocalDate.now();
    }
    @PreUpdate
    public void preUpdate()
    {
        this.updated = LocalDateTime.now();
    }
}

