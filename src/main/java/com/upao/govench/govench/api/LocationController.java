package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.LocationRequestDTO;
import com.upao.govench.govench.model.dto.LocationResponseDTO;
import com.upao.govench.govench.service.impl.LocationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/locations")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class LocationController {

    private final LocationServiceImpl locationServiceImpl;

    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        List<LocationResponseDTO> locations = locationServiceImpl.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Integer id) {
        LocationResponseDTO location = locationServiceImpl.getLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody LocationRequestDTO locationRequestDTO) {
        LocationResponseDTO location = locationServiceImpl.createLocation(locationRequestDTO);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable Integer id,
                                                              @RequestBody LocationRequestDTO locationRequestDTO) {
        LocationResponseDTO location = locationServiceImpl.updateLocation(id, locationRequestDTO);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationServiceImpl.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
