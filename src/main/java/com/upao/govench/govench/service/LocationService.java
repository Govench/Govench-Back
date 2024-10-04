package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.LocationRequestDTO;
import com.upao.govench.govench.model.dto.LocationResponseDTO;

import java.util.List;

public interface LocationService {
    public List<LocationResponseDTO> getAllLocations();
    public LocationResponseDTO getLocationById(Integer id);
    public LocationResponseDTO createLocation(LocationRequestDTO location);
    public LocationResponseDTO updateLocation(Integer id, LocationRequestDTO locationRequestDTO);
    public void deleteLocation(Integer id);
}
