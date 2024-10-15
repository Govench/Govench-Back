package com.upao.govench.govench.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id_in")
    private int role_id;
    @Column(name = "rol_nam_vc")
    private String role_name;
}
