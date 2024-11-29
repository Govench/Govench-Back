package com.upao.govench.govench.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventResponseDTO {
    
    public String tittle;
    public LocationResponseDTO location;
    public String type;
    public LocalDate date;
    public LocalTime startTime;
    public LocalDate registrationDate;
    public String link;
    public boolean deleted;
    public int eventId;
}
