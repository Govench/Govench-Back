package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.LocationRequestDTO;
import com.upao.govench.govench.model.dto.LocationResponseDTO;
import com.upao.govench.govench.model.entity.Location;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class LocationMapper {

    private ModelMapper modelMapper;

    public Location convertToEntity(LocationRequestDTO locationRequestDTO) {
        return modelMapper.map(locationRequestDTO, Location.class);
    }
    public Location convertToEntity(LocationResponseDTO locationRequestDTO) {
        return modelMapper.map(locationRequestDTO, Location.class);
    }

    public LocationResponseDTO convertToDTO(Location location) {
        return modelMapper.map(location, LocationResponseDTO.class);
    }

    public List<LocationResponseDTO> convertToListDTO(List<Location> locations) {
        return locations.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
