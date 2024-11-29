package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Participant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "par_id_in")
    private Integer id;

    @Column(name = "par_nam_vc", nullable = false, length = 100)
    private String name;

    @Column(name = "par_las_nam_vc", nullable = false, length = 100)
    private String lastname;

    @Column(name="par_birth_date_dt", nullable = false)
    private LocalDate birthday;

    @Column(name="par_gen_bi", nullable = false)
    private String gender;

    @Column(name="par_des_vc")
    private String profileDesc;

    @ElementCollection
    private List<String> interest;

    @ElementCollection
    private List<String> skills;

    @ElementCollection
    private List<String> socialLinks;

    @JsonIgnore
    @Column(name="par_cre_dt")
    private LocalDateTime created;

    @Column(name = "use_profile_id", nullable = true)
    private String profileId; // Referencia al ID del perfil en MongoDB

    @OneToOne
    @JoinColumn(name = "par_use_id_in" ,referencedColumnName = "user_id")
    @JsonIgnore
    private User user;

}
