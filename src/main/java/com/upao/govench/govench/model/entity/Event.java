package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    //Aca se agregaria la Entidad user, pero como no lo tengo implementado aun no
}
