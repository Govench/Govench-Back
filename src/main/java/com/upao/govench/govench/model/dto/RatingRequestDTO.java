package com.upao.govench.govench.model.dto;


import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RatingRequestDTO {
    private Integer ratingValue;
    private String comment;
}
