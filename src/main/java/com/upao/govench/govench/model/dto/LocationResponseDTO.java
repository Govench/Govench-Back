package com.upao.govench.govench.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDTO {

    private Integer id;
    private String departament;
    private String province;
    private String district;
    private String address;


}
