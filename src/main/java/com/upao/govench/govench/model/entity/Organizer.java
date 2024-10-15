package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="Organizer")
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "or_id_in")
    private Integer id;

    @Column(name = "or_nam_vc", nullable = false, length = 100)
    private String name;

    @Column(name = "or_las_nam_vc", nullable = false, length = 100)
    private String last_name;

    @Column(name="or_birth_date_dt", nullable = false)
    private Date birthday;

    @Column(name="or_gen_bi", nullable = false)
    private String gender;

    @Column(name="or_des_vc", nullable = false)
    private String profileDesc;

    @ElementCollection
    private List<String> interest;

    @ElementCollection
    private List<String> skills;

    @ElementCollection
    private List<String> socialLinks;

    @JsonIgnore
    @ManyToMany(mappedBy = "followings")
    private List<Organizer> followers;

    @Column(name = "or_profile_id", nullable = true)
    private String profileId; // Referencia al ID del perfil en MongoDB

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "UserFollow",
            joinColumns = @JoinColumn(name = "use_id_fwer_in"),
            inverseJoinColumns = @JoinColumn(name = "use_id_fwed_in"))
    private List<Organizer> followings;

    @JsonIgnore
    @OneToMany(mappedBy = "ratedUser")
    private List<Rating> receivedRatings;

    @JsonIgnore
    @OneToMany(mappedBy = "raterUser")
    private List<Rating> givenRatings;

    @OneToOne
    @JoinColumn(name = "or_use_id_in" ,referencedColumnName = "user_id")
    private User user;

}
