package com.upao.govench.govench.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RatingResponseDTO {
    private Integer ratingValue;
    private String UserCalificado;
    private String comment;
}
