package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="User_profile")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="use_id_in")
    private Integer id;

    @Column(name="use_nam_vc", nullable = false, length=100)
    private String name;

    @Column(name="use_ema_vc", nullable = false, length=100)
    private String email;

    @Column(name="use_pas_vc", nullable = false)
    private String password;

    @Column(name="use_birth_date_dt", nullable = false)
    private Date birthday;

    @Column(name="use_gen_bi", nullable = false)
    private String gender;

    @Column(name="use_des_vc", nullable = false)
    private String profileDesc;

    @ElementCollection
    private List<String> interest;

    @ElementCollection
    private List<String> skills;

    @ElementCollection
    private List<String> socialLinks;

    @ElementCollection
    private List<String> followers;

    @ElementCollection
    private List<String> followed;
}
