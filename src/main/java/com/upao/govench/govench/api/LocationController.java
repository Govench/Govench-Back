package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.LocationRequestDTO;
import com.upao.govench.govench.model.dto.LocationResponseDTO;
import com.upao.govench.govench.service.impl.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        List<LocationResponseDTO> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Integer id) {
        LocationResponseDTO location = locationService.getLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody LocationRequestDTO locationRequestDTO) {
        LocationResponseDTO location = locationService.createLocation(locationRequestDTO);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable Integer id,
                                                              @RequestBody LocationRequestDTO locationRequestDTO) {
        LocationResponseDTO location = locationService.updateLocation(id, locationRequestDTO);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
