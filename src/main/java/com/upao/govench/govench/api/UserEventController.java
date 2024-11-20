package com.upao.govench.govench.api;

import com.upao.govench.govench.mapper.UserEventMapper;
import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.dto.EventResponseDTO;
import com.upao.govench.govench.model.dto.OwnerResponseDTO;
import com.upao.govench.govench.model.dto.ParticipantDTO;
import com.upao.govench.govench.model.dto.UserEventResponseDTO;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.EventRepository;
import com.upao.govench.govench.service.PaypalService;
import com.upao.govench.govench.service.UserService;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.service.UserEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/inscription")
@PreAuthorize("hasAnyRole('PARTICIPANT', 'ORGANIZER')")
public class UserEventController{
    @Autowired
    public PaypalService paypalService;
    @Autowired
    private EventServiceImpl eventService;
    @Autowired
    private UserEventService userEventService;
    @Autowired
    private EventServiceImpl eventServiceImpl;
    @Autowired
    private UserService userService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserEventMapper userEventMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/myInscriptions")
    public List<UserEventResponseDTO> getMyInscriptions() {
        Integer iduser = userService.getAuthenticatedUserIdFromJWT();
        User user= userService.getUserbyId(iduser);
        return userEventMapper.userEventResponseDTOList(userEventService.getUserEventbyUser(user));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{iduser}")
    public List<UserEventResponseDTO> getUserEventbyUser(@PathVariable int iduser) {
        User user= userService.getUserbyId(iduser);
        return userEventMapper.userEventResponseDTOList(userEventService.getUserEventbyUser(user));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/event/{idevent}")
    public List<OwnerResponseDTO> getUserEventbyEvent(@PathVariable int idevent) {
        Event event= eventServiceImpl.getEventById(idevent);
        return userEventMapper.userOwnerResponseDTOList(userEventService.getUserEventbyEvent(event));
    }


    @PostMapping("/{idevent}")
    public ResponseEntity<String> registerOrPayForEvent(@PathVariable int idevent) {
        Integer iduser = userService.getAuthenticatedUserIdFromJWT();
        User user = userService.getUserbyId(iduser);
        Event event = eventService.getEventById(idevent);

        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        if (event == null) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
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

            if (event.getCost().doubleValue() != 0 && !user.getPremium()) {
                String paymentUrl = handleEventPayment(idevent);
                return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
            }
            // Crear la nueva relación entre usuario y evento
            UserEvent createdUserEvent = userEventService.addUserEvent(
                    new UserEvent(new IdCompuestoU_E(iduser, idevent), // ID compuesto
                            user, // Usuario asociado
                            event, // Evento asociado
                            LocalDate.now(), // Fecha de registro
                            false, // Notificaciones habilitadas (por defecto, false)
                            null, // Fecha del último recordatorio enviado (inicialmente null)
                            false, // Recordatorio del mismo día enviado (inicialmente false)
                            false  // Recordatorio final enviado (inicialmente false))
                    )
            );

            return new ResponseEntity<>("Inscripción exitosa", HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>("No se puede realizar la inscripción porque el evento ya no acepta más inscripciones.", HttpStatus.BAD_REQUEST);
        }
    }

    private String handleEventPayment(int eventId) {
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        String returnUrl = "https://govench-api.onrender.com/api/v1/payments/payment?eventId=" + eventId + "&userId=" + userId.toString();
        //String returnUrl = "https://govench-api.onrender.com/api/v1/payments/payment?eventId=" + eventId + "&userId=" + userId.toString();
        String cancelUrl = "https://govench-fb742.web.app/pago/cancelado";
        //
        BigDecimal eventCost = eventService.getEventById(eventId).getCost();
        try {
            String approvalUrl = paypalService.createOrder(eventCost.doubleValue(), returnUrl, cancelUrl);
            return "https://www.sandbox.paypal.com/checkoutnow?token=" + approvalUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar el pago.";
        }
    }

    @DeleteMapping("/{idevent}")
    public ResponseEntity<String> deleteUserEvent(@PathVariable int idevent) {

        Integer iduser = userService.getAuthenticatedUserIdFromJWT();
        // Verificar si el usuario existe
        User user = userService.getUserbyId(iduser);
        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        // Verificar si el evento existe
        Event event = eventServiceImpl.getEventById(idevent);
        if (event == null) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
        }

        IdCompuestoU_E id = new IdCompuestoU_E(iduser, idevent);
        // Verificar si la relación existe
        UserEvent existingEvent = userEventService.searchUserEventById(new IdCompuestoU_E(iduser, idevent));
        if (existingEvent == null) {
            return new ResponseEntity<>("No estas registrado en este evento", HttpStatus.CONFLICT);
        }

        LocalDateTime localDateTime = LocalDateTime.of(event.getDate(), event.getStartTime());
        // Verificar si el evento aún no ha comenzado
        if (localDateTime.isAfter(LocalDateTime.now())) {
            // Eliminar la inscripción del usuario al evento
            userEventService.removeUserEventById(id);
            // Restar uno al contador de inscritos del evento y guardar los cambios
            event.setRegisteredCount(event.getRegisteredCount() - 1);
            eventRepository.save(event);

            return new ResponseEntity<>("Se ha eliminado la inscripción correctamente.", HttpStatus.OK);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No se puede cancelar la inscripción porque el evento ya ha comenzado."
            );
        }
    }


    @GetMapping("/history")
    public ResponseEntity<List<EventResponseDTO>> getEventHistoryByUserId() {
        // Obtener el ID del usuario autenticado desde el token JWT
        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<EventResponseDTO> eventHistory = userEventService.getEventHistory(userId);
        if (eventHistory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(eventHistory, HttpStatus.OK);
    }

    @GetMapping("/events/{idevent}/participants")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<?> getParticipantsByEvent(@PathVariable int idevent) {
        // Obtener el ID del usuario autenticado desde el token JWT
        Integer iduser = userService.getAuthenticatedUserIdFromJWT();
        if (iduser == null) {
            return new ResponseEntity<>("Usuario no autenticado", HttpStatus.UNAUTHORIZED);
        }

        // Consultar el usuario por su ID
        User user = userService.getUserbyId(iduser);
        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        // Verificar si el evento existe
        Event event = eventServiceImpl.getEventById(idevent);
        if (event == null) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
        }

        // Verificar que sea solo el rol organizador



        // Verificar que el usuario sea el creador del evento
        if (!user.getId().equals(event.getOwner().getId())) {
            return new ResponseEntity<>("No tienes permisos para ver los participantes de este evento", HttpStatus.FORBIDDEN);
        }

        // Obtener la lista de participantes inscritos en el evento
        List<ParticipantDTO> participants = userMapper.getParticipantsByEvent(userEventService.getParticipantsByEvent(idevent));
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

}