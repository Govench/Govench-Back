package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class UserProfileDTO {
    private Integer id;
    private String email;
    private Role role;

    private String name;

    private String lastname;

    private int seguidores;

    private int seguidos;

    private String profileDesc;

    private LocalDate birthday;

    private String gender;

    private List<String> interest;

    private List<String> skills;

    private List<String> socialLinks;

    //este solo sera para el organizador
    private int eventosCreados;

    private String tipoUsuario;

}
