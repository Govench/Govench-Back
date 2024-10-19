package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @Column(name = "eve_id_in")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "eve_tit_vc", nullable = false)
    private String tittle;

    @Column(name = "eve_des_vc", nullable = false)
    private String description;

    @Column(name = "eve_dat_da", nullable = false)
    private LocalDate date;

    @Column(name = "eve_start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "eve_end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "eve_sta_vc", nullable = false)
    private String state;

    @Column(name = "eve_typ_vc", nullable = false)
    private String type;

    @Column(name = "eve_cos_mo", nullable = false)
    private BigDecimal cost;

    @Column(name = "eve_lvl_exp_vc", nullable = false)
    private String exp;

    @ManyToOne
    @JoinColumn(name = "loc_id_in", nullable = true )
    private Location location;

    @Column(name = "eve_last_rem_da")
    private LocalDate lastReminderSentDate;

    @Column(name = "eve_same_day_rem_bo", nullable = true)
    private boolean sameDayReminderSent = false;

    @Column(name = "eve_fin_rem_bo", nullable = true)
    private boolean finalReminderSent = false;

    // Nuevos atributos
    @Column(name = "eve_max_cap_in", nullable = false)
    private int maxCapacity;

    @Column(name = "eve_reg_count_in", nullable = false)
    private int registeredCount = 0;


}
