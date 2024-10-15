package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "User_Event")
public class UserEvent {
    @EmbeddedId
    @Column(name = "use_eve_id_in")
    private IdCompuestoU_E id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("use_id_in")
    @JoinColumn(name = "use_id_in")
    private Participant user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("eve_id_in")
    @JoinColumn(name = "eve_id_in")
    private Event event;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "use_eve_notif_act_bo")
    private boolean notificationsEnabled = false;



    @PrePersist
    public void prePersist() {
        this.registrationDate = LocalDate.now();
    }

}