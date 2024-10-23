package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private Integer id;
    private String email;
    private Role role;

    private String name;
    private String lastname;

    private String profileDesc;

    private List<String> interest;

    private List<String> skills;

    private List<String> socialLinks;

    //este solo sera para el organizador
    private int eventosCreados;

    private String tipoUsuario;

}
