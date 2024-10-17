package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.OrganizerDTO;
import com.upao.govench.govench.service.AdminOrganizerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/organizer")
@PreAuthorize("hasRole('ADMIN')")  //Solo el rol admin puede acceder a estos enpoints
public class AdminOrganizerController {
    @Autowired
    private AdminOrganizerService adminOrganizerService;

    @GetMapping
    public ResponseEntity<List<OrganizerDTO>> listAll() {
        List<OrganizerDTO> authors = adminOrganizerService.getAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<OrganizerDTO> create(@Valid @RequestBody OrganizerDTO organizerDTO) {
        OrganizerDTO createOrganizer = adminOrganizerService.save(organizerDTO);
        return new ResponseEntity<>(createOrganizer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizerDTO> getById(@PathVariable Integer id) {
        OrganizerDTO organizerDTO = adminOrganizerService.findById(id);
        return new ResponseEntity<>(organizerDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizerDTO> update(@PathVariable Integer id, @Valid @RequestBody OrganizerDTO authorDTO) {
        OrganizerDTO updatedOrganizer = adminOrganizerService.update(id, authorDTO);
        return new ResponseEntity<>(updatedOrganizer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        adminOrganizerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}