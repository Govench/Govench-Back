package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEventResponseDTO {
    private int valorPuntuacion;
    private LocalDate fechaPuntuacion;
    private User user;
    private Event event;


}
