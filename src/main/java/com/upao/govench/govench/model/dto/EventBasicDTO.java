package com.upao.govench.govench.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor // Constructor necesario para JPQL
@NoArgsConstructor
public class EventBasicDTO {
    private int id;
    private String tittle;
    private LocalDate date;
}
