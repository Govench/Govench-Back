package com.upao.govench.govench.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class ParticipantDTO {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre debe tener 50 caracteres o menos")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido debe tener 50 caracteres o menos")
    private String lastname;

    private LocalDate birthday;

    private String gender;
    @NotBlank(message = "La descripcion es obligatorio")
    @Size(max = 500, message = "La descripcion debe tener 500 caracteres o menos")
    private String profileDesc;

    private List<FollowUserDTO> followers;
    private List<FollowUserDTO> following;

}
