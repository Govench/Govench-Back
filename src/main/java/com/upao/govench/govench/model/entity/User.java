package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "UsuarioGovench")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;

    @Column(name="use_ema_vc", nullable = false, length=100)
    private String email;

    @Column(name = "use_pas_vc", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Participant participant;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Organizer organizer;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Admin admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id_in" , referencedColumnName = "rol_id")
    private Role role;

    @Column(name="use_pre_bo", nullable = true)
    private Boolean  premium;

    public Integer getId() {
        System.out.println("Accediendo al ID del usuario: " + id);
        return id;
    }

    public void setId(Integer id) {
        System.out.println("Estableciendo el ID del usuario: " + id);
        this.id = id;
    }

    public String getEmail() {
        System.out.println("Accediendo al email del usuario: " + email);
        return email;
    }

    public void setEmail(String email) {
        System.out.println("Estableciendo el email del usuario: " + email);
        this.email = email;
    }

    public Participant getParticipant() {
        System.out.println("Accediendo al participant del usuario.");
        return participant;
    }

    public void setParticipant(Participant participant) {
        System.out.println("Estableciendo el participant del usuario.");
        this.participant = participant;
    }

    public Organizer getOrganizer() {
        System.out.println("Accediendo al organizer del usuario.");
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        System.out.println("Estableciendo el organizer del usuario.");
        this.organizer = organizer;
    }

    public Boolean getPremium() {
        System.out.println("Accediendo al estado premium: " + premium);
        return premium;
    }

    public void setPremiun(Boolean premium) {
        System.out.println("Estableciendo el estado premium: " + premium);
        this.premium = premium;
    }
}




