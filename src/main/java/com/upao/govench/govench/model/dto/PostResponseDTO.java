package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private int id;
    private String body;
    private AutorResponseDTO autor; //Deberia usarse OwnerResponseDTO
    private LocalDate created;
    private LocalDateTime updated;

}
