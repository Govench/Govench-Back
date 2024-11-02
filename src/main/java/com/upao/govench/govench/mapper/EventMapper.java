package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.Location;
import com.upao.govench.govench.model.entity.Rating;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.LocationRepository;
import com.upao.govench.govench.repository.UserRepository;
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
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final LocationMapper locationMapper;

    public Event convertToEntity(EventRequestDTO eventRequestDTO) {
        Event event = modelMapper.map(eventRequestDTO, Event.class);


        Location location = locationRepository
                .findByAddressAndDistrictAndProvinceAndDepartament(
                        eventRequestDTO.getAddress(),
                        eventRequestDTO.getDistrict(),
                        eventRequestDTO.getProvince(),
                        eventRequestDTO.getDepartment()
                ).orElseGet(() -> {
                    LocationRequestDTO locationDTO = new LocationRequestDTO();
                    locationDTO.setAddress(eventRequestDTO.getAddress());
                    locationDTO.setDistrict(eventRequestDTO.getDistrict());
                    locationDTO.setProvince(eventRequestDTO.getProvince());
                    locationDTO.setDepartament(eventRequestDTO.getDepartment());

                    // Crear y guardar la nueva ubicación en el repositorio
                    Location newLocation = locationMapper.convertToEntity(locationDTO);
                    return locationRepository.save(newLocation); // Guardar y retornar la ubicación
                });
        event.setLocation(location);
         User owner = userService.getUserbyId((userService.getAuthenticatedUserIdFromJWT()));
        // Mapear el propietario
        if (owner != null) {
            event.setOwner(owner);
        }
        return event;
    }

    public EventResponseDTO convertToDTO(Event event) {
        EventResponseDTO eventResponseDTO =  modelMapper.map(event, EventResponseDTO.class);
        if (event.getOwner() != null) {
            eventResponseDTO.setOwnerId(event.getOwner().getId());
        }
        return eventResponseDTO;
    }

    public List<EventResponseDTO> convertToListDTO(List<Event> events) {
        return events.stream()
                .map(this::convertToDTO)
                .toList();
    }


}
