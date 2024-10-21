package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.RatingDTO;
import com.upao.govench.govench.model.entity.Rating;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingMapper {

    public RatingDTO convertToDTO(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setIdRating(rating.getId());
        ratingDTO.setRatingValue(rating.getRatingValue());
        ratingDTO.setComment(rating.getComment());

        // Mapear nombre y apellido basado en el tipo de usuario que califica
        if (rating.getRaterParticipant() != null) {
            ratingDTO.setName(rating.getRaterParticipant().getName());
            ratingDTO.setLastname(rating.getRaterParticipant().getLastname());
        } else if (rating.getRaterOrganizer() != null) {
            ratingDTO.setName(rating.getRaterOrganizer().getName());
            ratingDTO.setLastname(rating.getRaterOrganizer().getLastname());
        }

        return ratingDTO;
    }

    public List<RatingDTO> convertToListDTO(List<Rating> ratings) {
        return ratings.stream()
                .map(this::convertToDTO)
                .toList();
    }
}

