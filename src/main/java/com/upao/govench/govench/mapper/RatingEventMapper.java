package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.RatingEventRequestDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.RatingEvent;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RatingEventMapper {

    private final ModelMapper modelMapper;

    public RatingEvent convertToEntity(RatingEventRequestDTO ratingEventRequestDTO) {
        return modelMapper.map(ratingEventRequestDTO, RatingEvent.class);
    }

    public RatingEventResponseDTO convertToDTO(RatingEvent ratingEvent) {
        User user =ratingEvent.getUserId();
        RatingEventResponseDTO ratingEventResponseDTO = modelMapper.map(ratingEvent, RatingEventResponseDTO.class);
        ratingEventResponseDTO.setEmail(user.getEmail());
        if(user.getOrganizer()!=null)
        {
            ratingEventResponseDTO.setName(user.getOrganizer().getName());
            ratingEventResponseDTO.setLastname(user.getOrganizer().getLastname());
        }
        if(user.getParticipant()!=null)
        {
            ratingEventResponseDTO.setName(user.getParticipant().getName());
            ratingEventResponseDTO.setLastname(user.getParticipant().getLastname());
        }
        return ratingEventResponseDTO;
    }

    public List<RatingEventResponseDTO> convertToListDTO(List<RatingEvent> ratingEvents) {
     return ratingEvents.stream()
             .map(this::convertToDTO)
             .toList();
    }
}
