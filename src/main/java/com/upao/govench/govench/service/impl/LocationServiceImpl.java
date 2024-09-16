package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.LocationMapper;
import com.upao.govench.govench.model.dto.LocationRequestDTO;
import com.upao.govench.govench.model.dto.LocationResponseDTO;
import com.upao.govench.govench.model.entity.Location;
import com.upao.govench.govench.repository.LocationRepository;
import com.upao.govench.govench.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional(readOnly = true)
    public List<LocationResponseDTO> getAllLocations(){
        List<Location> locations = locationRepository.findAll();
        return locationMapper.convertToListDTO(locations);
    }

    @Transactional(readOnly = true)
    public LocationResponseDTO getLocationById(Integer id){
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localizacion no encontrada con el numero de ID"+id));
        return locationMapper.convertToDTO(location);
    }

    @Transactional
    public LocationResponseDTO createLocation(LocationRequestDTO locationRequestDTO){
        Location location = locationMapper.convertToEntity(locationRequestDTO);
        locationRepository.save(location);
        return locationMapper.convertToDTO(location);
    }

    @Transactional
    public LocationResponseDTO updateLocation(Integer id, LocationRequestDTO locationRequestDTO){
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localizacion no encontrada con el numero de ID"+id));
        if(locationRequestDTO.getDepartament()!= null)location.setDepartament(locationRequestDTO.getDepartament());
        if(locationRequestDTO.getProvince()!= null)location.setProvince(locationRequestDTO.getProvince());
        if(locationRequestDTO.getDistrict()!= null)location.setDistrict(locationRequestDTO.getDistrict());
        if(locationRequestDTO.getAddress()!= null)location.setAddress(locationRequestDTO.getAddress());

        locationRepository.save(location);

        return locationMapper.convertToDTO(location);
    }

    @Transactional
    public void deleteLocation(Integer id){
        locationRepository.deleteById(id);
    }
}
