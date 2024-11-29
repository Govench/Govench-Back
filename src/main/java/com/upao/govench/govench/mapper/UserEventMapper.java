package com.upao.govench.govench.mapper;


import com.upao.govench.govench.model.dto.OwnerResponseDTO;
import com.upao.govench.govench.model.dto.UserEventResponseDTO;
import com.upao.govench.govench.model.entity.UserEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Data
@Component
public class UserEventMapper {
    private final ModelMapper modelMapper;
    private final LocationMapper locationMapper;

    private UserEventResponseDTO ToResponseDto(UserEvent userEvent) {
        UserEventResponseDTO userEventResponseDTO =  modelMapper.map(userEvent, UserEventResponseDTO.class);

        userEventResponseDTO.setLocation(locationMapper.convertToDTO(userEvent.getEvent().getLocation()));
        userEventResponseDTO.setTittle(userEvent.getEvent().getTittle());
        userEventResponseDTO.setType(userEvent.getEvent().getType());
        userEventResponseDTO.setDate(userEvent.getEvent().getDate());
        userEventResponseDTO.setStartTime(userEvent.getEvent().getStartTime());
        userEventResponseDTO.setRegistrationDate(userEvent.getRegistrationDate());
        userEventResponseDTO.setLink(userEvent.getEvent().getLink());
        userEventResponseDTO.setDeleted(userEvent.getEvent().getstatusdeleted());
        userEventResponseDTO.setEventId(userEvent.getEvent().getId());
        return userEventResponseDTO;
    }

    private OwnerResponseDTO ToUserResponseDto(UserEvent userEvent) {
        OwnerResponseDTO ownerResponseDTO =  new OwnerResponseDTO();
        ownerResponseDTO.setId(userEvent.getUser().getId());
        ownerResponseDTO.setEmail(userEvent.getUser().getEmail());
        if(userEvent.getUser().getParticipant() != null)
        {
            ownerResponseDTO.setProfileDesc(userEvent.getUser().getParticipant().getProfileDesc());
            ownerResponseDTO.setName(userEvent.getUser().getParticipant().getName());
        }
        if(userEvent.getUser().getOrganizer() != null)
        {
            ownerResponseDTO.setProfileDesc(userEvent.getUser().getOrganizer().getProfileDesc());
            ownerResponseDTO.setName(userEvent.getUser().getOrganizer().getName());
        }

        return ownerResponseDTO;
    }

    public List<UserEventResponseDTO> userEventResponseDTOList(List<UserEvent> userEvent) {
        return userEvent.stream()
                .map(this::ToResponseDto)
                .toList();
    }
    public List<OwnerResponseDTO> userOwnerResponseDTOList(List<UserEvent> userEvent) {
        return userEvent.stream()
                .map(this::ToUserResponseDto)
                .toList();
    }
}
