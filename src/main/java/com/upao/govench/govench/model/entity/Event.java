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
    @Column(name = "eve_id_in")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "eve_tit_vc", nullable = false)
    private String tittle;

    @Column(name = "eve_img_vc", nullable = true)
    private String coverPath;

    @Column(name = "eve_des_vc", nullable = false)
    private String description;

    @Column(name = "eve_dat_da", nullable = false)
    private LocalDate date;

    @Column(name = "eve_start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "eve_end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "eve_typ_vc", nullable = false)
    private String type;

    @Column(name = "eve_cos_mo", nullable = false)
    private BigDecimal cost;

    @Column(name = "eve_lvl_exp_vc", nullable = false)
    private String exp;

    @ManyToOne
    @JoinColumn(name = "loc_id_in", nullable = true )
    private Location location;

    // Nuevos atributos
    @Column(name = "eve_max_cap_in", nullable = false)
    private int maxCapacity;

    @Column(name = "eve_reg_count_in", nullable = false)
    private int registeredCount = 0;
    @Column(name ="eve_link_vc", nullable = true)
    private String link;
    @ManyToOne
    @JoinColumn(name = "com_own_vc", nullable = false, referencedColumnName = "user_id")
    private User owner;

    @Column(name = "eve_deleted_bo", nullable = true)
    private boolean deleted = false;

    public  boolean getstatusdeleted()
    {
        return this.deleted;
    }
}
