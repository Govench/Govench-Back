package com.upao.govench.govench.model.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDTO {

    @NotBlank(message = "El titulo no puede estar vacio")
    private String tittle;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String description;

    @NotBlank(message = "La fecha no puede estar vacia")
    //@Future(message = "La fecha debe ser una fecha futura")
    private LocalDate date;

    @NotBlank(message = "La hora de inicio no puede estar vacía")
    private LocalTime startDate;

    @NotBlank(message = "La hora de fin no puede estar vacía")
    private LocalTime endDate;

    @NotBlank(message = "El estado no puede estar vacío")
    private String state;

    @NotBlank(message = "El tipo no puede estar vacio")
    private String type;

    private BigDecimal cost;

}
