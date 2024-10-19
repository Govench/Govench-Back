package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El autor no puede ser nulo")
    private User autor;

    @NotNull(message = "La comunidad no puede ser nula")
    private Community comunidad;

    private LocalDate created;
}


