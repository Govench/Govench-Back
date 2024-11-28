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
@Table(name = "User_Community")
public class UserCommunity {
    @EmbeddedId
    @Column(name = "use_com_id_in")
    private IdCompuestoU_C id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("use_id_in")
    @JoinColumn(name = "use_id_in",referencedColumnName = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("com_id_in")
    @JoinColumn(name = "com_id_in")
    private Community community;

    @Column(name = "ins_dat_da")
    private LocalDate date;

    @PrePersist
    public void prePersist() {
        date = LocalDate.now();
    }
}