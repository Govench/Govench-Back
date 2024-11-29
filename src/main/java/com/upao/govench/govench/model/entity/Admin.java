package com.upao.govench.govench.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "govenchAdmin")
public class Admin {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="adm_id_in")
    private Integer admin_id;

    @OneToOne
    @JoinColumn(name = "ad_use_id_in" ,referencedColumnName = "user_id")
    @JsonIgnore
    private User user;

}
