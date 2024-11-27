package com.upao.govench.govench.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {

    private Long id;
    private String tittle;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String type;
    private BigDecimal cost;
    private LocationResponseDTO location;
    private String exp;
    private String coverPath;
    private String link;
    private Integer maxCapacity;
    private int registeredCount;
    private Integer ownerId;
    private boolean deleted;
}
