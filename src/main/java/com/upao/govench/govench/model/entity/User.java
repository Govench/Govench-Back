package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "govenchUser")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="use_id_in")
    private Integer user_id;

    @Column(name="use_ema_vc", nullable = false, length=100)
    private String email;

    @Column(name="use_pas_vc", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Participant participant;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Organizer organizer;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Admin admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id_in" , referencedColumnName = "role_id")
    private Role role;
}
