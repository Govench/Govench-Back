package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.service.UserService;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import com.upao.govench.govench.service.impl.UserEventServiceImpl;
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
    private EventServiceImpl eventService;
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

    @PostMapping("/{iduser}/{idevent}")
    public ResponseEntity<String> createUserEvent(@PathVariable int iduser, @PathVariable int idevent) {
        // Consultar el usuario por su ID
        User user = userService.getUserbyId(iduser);
        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND); // Si el usuario no existe
        }

        // Consultar el evento por su ID
        Event event = eventService.getEventById(idevent);
        if (event == null) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND); // Si el evento no existe
        }

        // Verificar si la relación ya existe
        UserEvent existingEvent = userEventService.searchUserEventById(new IdCompuestoU_E(iduser, idevent));
        if (existingEvent != null) {
            return new ResponseEntity<>("Ya estás registrado en este evento", HttpStatus.CONFLICT); // Si la relación ya existe
        }

        // Verificar si el evento todavía permite inscripciones (según fecha y hora)
        LocalTime systemTime = LocalTime.now();
        LocalDate systemDate = LocalDate.now();

        if (systemDate.isBefore(event.getDate()) ||
                (systemDate.isEqual(event.getDate()) && systemTime.isBefore(event.getStartTime()))) {

            // Crear la nueva relación entre usuario y evento
            UserEvent createdUserEvent = userEventService.addUserEvent(
                    new UserEvent(new IdCompuestoU_E(iduser, idevent), user, event, LocalDate.now(), false)
            );

            return new ResponseEntity<>("Inscripción exitosa", HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>("No se puede realizar la inscripción porque el evento ya no acepta más inscripciones.", HttpStatus.BAD_REQUEST);
        }
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


    // Este endpoint devuelve el historial de eventos de un usuario por su ID
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<EventResponseDTO>> getEventHistoryByUserId(@PathVariable Integer userId) {
        List<EventResponseDTO> eventHistory = userEventService.getEventHistory(userId);
        if (eventHistory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(eventHistory, HttpStatus.OK);
    }

}