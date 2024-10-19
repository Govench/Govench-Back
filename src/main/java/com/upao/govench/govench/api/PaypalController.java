package com.upao.govench.govench.api;

import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.IdCompuestoU_E;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserEvent;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.service.UserEventService;
import com.upao.govench.govench.service.UserService;
import com.upao.govench.govench.service.impl.EventServiceImpl;
import com.upao.govench.govench.service.PaypalService;
import com.upao.govench.govench.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/payments")

public class PaypalController {
    @Autowired
    public PaypalService paypalService;
    @Autowired
    private EventServiceImpl eventServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserEventController userEventController;
    @Autowired
    private UserService userService;
    @Autowired
    private EventServiceImpl eventService;
    @Autowired
    private UserEventService userEventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-order")
    public String  createOrder(@RequestParam double totalAmount) {
        String returnUrl = "http://localhost:8080/api/v1/admin/payments/payment";
        String cancelUrl = "https://blog.fluidui.com/top-404-error-page-examples/";
        try {
            String orderId = paypalService.createOrder(totalAmount, returnUrl, cancelUrl);
            if (orderId == null) {
                //return new RedirectView("/error?status=error");
            }

            String approvalUrl = "https://www.sandbox.paypal.com/checkoutnow?token=" + orderId;
            return approvalUrl;
            //return new RedirectView(approvalUrl);

        } catch (IOException e) {
            e.printStackTrace();
            //return new RedirectView("/error?status=error");
            return "/error?status=error";
        }
    }

    @GetMapping("/payment")
    public String handlePayment(@RequestParam String token, @RequestParam int eventId, @RequestParam int userId) {
        try {
            HttpResponse<Order> response = paypalService.captureOrder(token);

            if (response.statusCode() == 201) { // Pago exitoso
                try {
                    createUserEvent(userId, eventId);
                    return "Pago completado con éxito e inscripción realizada.";
                } catch (NotFoundException e) {
                    return "Pago completado, pero hubo un problema con la inscripción: " + e.getMessage();
                } catch (IllegalArgumentException e) {
                    return e.getMessage(); // Manejar errores de inscripción
                }
            } else {
                return "El pago fue cancelado o fallido.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Ocurrió un error durante el proceso de pago.";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','PARTICIPANT')")
    @GetMapping("/pay-event/{eventId}")
    public String handleEventPayment(@PathVariable int eventId) {

        // Retrieve event by ID
        Event event = eventServiceImpl.getEventById(eventId);
        Integer userId = userServiceImpl.getAuthenticatedUserIdFromJWT();
        LocalDateTime localDateTime = LocalDateTime.of(event.getDate(), event.getEndTime());
        String returnUrl = "http://localhost:8080/api/v1/admin/payments/payment?eventId=" + eventId+"&userId="+userId.toString();
        String cancelUrl = "https://blog.fluidui.com/top-404-error-page-examples/";

        if (event == null)
        {
            throw new NotFoundException("El evento no existe");
        }

        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El evento ya no está disponible.");
        }
        UserEvent existingEvent = userEventService.searchUserEventById(new IdCompuestoU_E(userId, eventId));
        if (existingEvent != null) {
            throw new IllegalArgumentException("Ya estas registrado en este evento");
        }
        // Check if the event has a price
        BigDecimal eventPrice = event.getCost();

        if (eventPrice.compareTo(BigDecimal.ZERO) == 0) {
            return "This event is free.";
        }
        double eventPriceDouble = eventPrice.doubleValue();
        // Generate the PayPal order for the event price
        try {
            String approvalUrl = paypalService.createOrder(eventPriceDouble, returnUrl, cancelUrl);
            return "https://www.sandbox.paypal.com/checkoutnow?token=" + approvalUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred during payment process.";
        }

    }

    public void createUserEvent(int iduser, int idevent) {
        // Consultar el usuario por su ID
        User user = userService.getUserbyId(iduser);
        if (user == null) {
            throw new NotFoundException("El usuario no existe");
              }

        // Consultar el evento por su ID
        Event event = eventService.getEventById(idevent);
        if (event == null) {
            throw new NotFoundException("El evento no existe");
             }

        // Verificar si la relación ya existe
        UserEvent existingEvent = userEventService.searchUserEventById(new IdCompuestoU_E(iduser, idevent));
        if (existingEvent != null) {
            throw new IllegalArgumentException("Ya estas registrado ");
               }

            // Crear la nueva relación entre usuario y evento
            UserEvent createdUserEvent = userEventService.addUserEvent(
                    new UserEvent(new IdCompuestoU_E(iduser, idevent), user, event, LocalDate.now(), false)
            );


    }

}

