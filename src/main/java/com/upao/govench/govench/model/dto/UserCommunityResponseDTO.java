package com.upao.govench.govench.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommunityResponseDTO {
    private Integer idCommunity;
    private String nameCommunity;
    private String descriptionCommunity;
    private LocalDate date;
}
