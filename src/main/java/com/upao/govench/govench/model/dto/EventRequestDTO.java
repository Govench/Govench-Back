package com.upao.govench.govench.model.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
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
    @Future(message = "La fecha debe ser una fecha futura")
    private LocalDate date;

    @NotBlank(message = "La hora de inicio no puede estar vacía")
    private LocalTime startTime;

    @NotBlank(message = "La hora de fin no puede estar vacía")
    private LocalTime endTime;

    @NotBlank(message = "El tipo no puede estar vacio")
    private String type;

    @NotBlank(message = "El nivel de experiencia no se puede estar vacio")
    private String exp;

    private String coverPath;//ruta de la imagen

    private BigDecimal cost;

    private String address;
    private String department;
    private String province;
    private String district;
    private String link;
    @NotNull(message = "La capacidad máxima no puede estar vacía")
    @Min(value = 1, message = "La capacidad máxima debe ser mayor a 0")
    private Integer maxCapacity;

    
}
