package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.RatingDTO;
import com.upao.govench.govench.model.dto.RatingRequestDTO;
import com.upao.govench.govench.model.dto.RatingResponseDTO;
import com.upao.govench.govench.model.entity.Rating;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Component
public class RatingMapper {

    private final ModelMapper modelMapper;

    public RatingResponseDTO toRatingResponseDTO(Rating rating) {
        if (rating == null) {
            return null;
        }
        RatingResponseDTO ratingResponseDTO = modelMapper.map(rating, RatingResponseDTO.class);


        if (rating.getRaterUser().getParticipant() != null) {

          //  ratingResponseDTO.setAutorname(rating.getRaterUser().getParticipant().getName());
           // ratingResponseDTO.setNombreUsuarioCalificado(rating.getRatedUser().getParticipant().getName());
            //ratingResponseDTO.setIdAutor(rating.getRaterUser().getParticipant().getId());

        }

        if (rating.getRaterUser().getOrganizer() != null) {

         //   ratingResponseDTO.setAutorname(rating.getRaterUser().getOrganizer().getName());
         //   ratingResponseDTO.setNombreUsuarioCalificado(rating.getRatedUser().getOrganizer().getName());
            //  ratingResponseDTO.setIdAutor(rating.getRaterUser().getOrganizer().getId());
        }

        return ratingResponseDTO;
    }

    public Rating toRating(RatingRequestDTO requestDTO) {
        if (requestDTO == null) {

            return null;
        }
        Rating rating = modelMapper.map(requestDTO, Rating.class);

        return rating;
    }

    public List<RatingResponseDTO> toRatingResponseDTOList(List<Rating> ratings) {
        return ratings.stream()
                .map(this::toRatingResponseDTO)
                .collect(Collectors.toList());
    }

}

