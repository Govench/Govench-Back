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
    @JoinColumn(name = "use_id_in",referencedColumnName = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("eve_id_in")
    @JoinColumn(name = "eve_id_in")
    private Event event;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "use_eve_notif_act_bo", nullable = false) // Para la notificiones
    private boolean notificationsEnabled = false;

    @Column(name = "eve_last_rem_da")  // Para la notificiones
    private LocalDate lastReminderSentDate;

    @Column(name = "eve_same_day_rem_bo", nullable = false) // Para la notificiones
    private boolean sameDayReminderSent = false;

    @Column(name = "eve_fin_rem_bo", nullable = false) // Para la notificiones
    private boolean finalReminderSent = false;

    @PrePersist
    public void prePersist() {
        this.registrationDate = LocalDate.now();
    }

}