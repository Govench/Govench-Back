package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "loc_dep_vac",nullable = false)
    private String departament;

    @Column(name = "loc_pro_vac",nullable = false)
    private String province;

    @Column(name = "loc_dis_vac",nullable = false)
    private String district;

    @Column(name = "loc_adr_vac",nullable = false)
    private String address;

}
