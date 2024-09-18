package com.upao.govench.govench.api;

import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.service.UserService;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.service.UserEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/inscription")
public class UserEventController{
    @Autowired
    private UserEventService userEventService;
    @Autowired
    private EventServiceImpl eventServiceImpl;
    @Autowired
    private UserService userService;
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserEvent> getAllUserEvents() {
        return userEventService.getAllUserEvents();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{iduser}")
    public List<UserEvent> getUserEventbyUser(@PathVariable int iduser) {
        User user= userService.getUserbyId(iduser);
        return userEventService.getUserEventbyUser(user);
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/event/{idevent}")
    public List<UserEvent> getUserEventbyEvent(@PathVariable int idevent) {
        Event event= eventServiceImpl.getEventById(idevent);
        return userEventService.getUserEventbyEvent(event);
    }

    @PostMapping
    public ResponseEntity<String> addUserEvent(@RequestBody UserEvent userEvent) {
        LocalTime systemTime = LocalTime.now();
        LocalDate systemDate= LocalDate.now();
        int id_event= userEvent.getEvent().getId();
        Event event = eventServiceImpl.getEventById(id_event);
        if(systemDate.isBefore(event.getDate()) ||
                (systemDate.isEqual(event.getDate()) && systemTime.isBefore(event.getStartTime())))
        {
             userEventService.addUserEvent(userEvent);
            return new ResponseEntity<>("Se ha inscrito correctamente.", HttpStatus.CREATED);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "No se realizar la inscripción porque el evento ya no acepta mas incscripciones."
        );
    }


    @DeleteMapping("/{iduser}/{idevent}")
    public ResponseEntity<String> deleteUserEvent(@PathVariable int iduser, @PathVariable int idevent) {
        IdCompuestoU_E id = new IdCompuestoU_E(iduser, idevent);
        Event event= eventServiceImpl.getEventById(idevent);
        LocalTime systemTime = LocalTime.now();
        LocalDate systemDate= LocalDate.now();
        if(systemDate.isBefore(event.getDate()) ||
                (systemDate.isEqual(event.getDate()) && systemTime.isBefore(event.getStartTime())))
        {
            userEventService.removeUserEventById(id);
            return new ResponseEntity<>("Se ha eliminado la inscripción correctamente.", HttpStatus.OK);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No se puede cancelar la inscripción porque el evento ya ha comenzado."
            );
        }
    }

}