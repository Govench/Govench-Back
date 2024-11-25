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

    public boolean getPremium()
    {
        return premium;
    }
    public void setPremiun(boolean valor)
    {
        this.premium = valor;
    }
}




