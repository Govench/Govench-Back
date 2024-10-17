package com.upao.govench.govench.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEventResponseDTO {
    private int id;
    private int valorPuntuacion;
    private LocalDate fechaPuntuacion;
    private int userId;
    private int eventId;
}
