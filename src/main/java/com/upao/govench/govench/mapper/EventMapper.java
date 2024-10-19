package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.EventRequestDTO;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.LocationResponseDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.Location;
import com.upao.govench.govench.model.entity.Rating;
import com.upao.govench.govench.service.EventService;
import com.upao.govench.govench.service.LocationService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EventMapper {

    private final ModelMapper modelMapper;
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public Event convertToEntity(EventRequestDTO eventRequestDTO) {
        Event event = modelMapper.map(eventRequestDTO, Event.class);

        LocationResponseDTO location = locationService.getLocationById(eventRequestDTO.getLocation().getId());

        event.setLocation(locationMapper.convertToEntity(location));
        return event;
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
