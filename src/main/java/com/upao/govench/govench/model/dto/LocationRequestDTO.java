package com.upao.govench.govench.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequestDTO {

    @NotBlank(message = "El nombre de departamento no puede estar vacio")
    private String departament;

    @NotBlank(message = "El nombre de provincia no puede estar vacio")
    private String province;

    @NotBlank(message = "El nombre de distrito no puede estar vacio")
    private String district;

    @NotBlank(message = "La direccion  no puede estar vacia")
    private String address;

}
