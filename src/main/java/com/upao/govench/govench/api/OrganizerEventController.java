package com.upao.govench.govench.api;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizator/events")
@AllArgsConstructor
@PreAuthorize("hasRole('ORGANIZER')")
public class OrganizerEventController {

    @GetMapping("/{id_user}/{id_events}")
    public void getEventsByOrganizer(int id_user, int id_events) {

    }
}
