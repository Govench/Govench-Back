package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.Participant;
import com.upao.govench.govench.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    @NotBlank(message = "La Descripcion no puede estar vacia")
    private String body;

    @NotBlank(message = "La id del autor no puede estar vacia")
    private User autor;

    @NotBlank(message = "La id de la comunidad no puede estar vacia")
    private Community comunidad;
    private LocalDate created;


}
