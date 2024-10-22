package com.upao.govench.govench.model.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Integer idUser;
    private Integer idRating;
    private String name;
    private String lastname;
    private Integer ratingValue;
    private String comment;
}
