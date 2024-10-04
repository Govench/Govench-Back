package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEventRequestDTO {

    @NotNull(message = "La puntuacion no puede estar vacia")
    @Pattern(regexp = "^[1-5]$", message = "La puntucion solo puede ser entre 0 y 5")
    private int valorPuntuacion;
}
