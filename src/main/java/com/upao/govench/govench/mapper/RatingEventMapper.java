package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.RatingEventRequestDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.dto.RatingRequestDTO;
import com.upao.govench.govench.model.entity.RatingEvent;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RatingEventMapper {

    private final ModelMapper modelMapper;

    public RatingEvent convertToEntity(RatingEventRequestDTO ratingEventRequestDTO) {
        return modelMapper.map(ratingEventRequestDTO, RatingEvent.class);
    }

    public RatingEventResponseDTO convertToDTO(RatingEvent ratingEvent) {
        return modelMapper.map(ratingEvent, RatingEventResponseDTO.class);
    }

    public List<RatingEventResponseDTO> convertToListDTO(List<RatingEvent> ratingEvents) {
     return ratingEvents.stream()
             .map(this::convertToDTO)
             .toList();
    }

}
