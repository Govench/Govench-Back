package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EventMapper {

    private final ModelMapper modelMapper;

    public Event convertToEntity(EventRequestDTO eventRequestDTO) {
        return modelMapper.map(eventRequestDTO, Event.class);
    }

    public EventResponseDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventResponseDTO.class);
    }

    public List<EventResponseDTO> convertToListDTO(List<Event> events) {
        return events.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
