package com.upao.govench.govench.api;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.service.UserEventService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/admin/userEvent")
public class UserEventController{
    @Autowired

    private UserEventService userEventService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserEvent> getAllUserEvents() {
        return userEventService.getAllUserEvents();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{iduser}/{idevent}")
    public UserEvent getUserEvent(@PathVariable int iduser, @PathVariable int idevent) {
        IdCompuestoU_E id = new IdCompuestoU_E(iduser, idevent);
        return userEventService.searchUserEventById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserEvent addUserEvent(@RequestBody UserEvent userEvent) {
        return userEventService.addUserEvent(userEvent);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{iduser}/{idevent}")
    public void deleteUserEvent(@PathVariable int iduser, @PathVariable int idevent) {
        IdCompuestoU_E id = new IdCompuestoU_E(iduser, idevent);
        userEventService.removeUserEventById(id);
    }

}