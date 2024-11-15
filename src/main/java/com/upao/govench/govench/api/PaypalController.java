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
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/payments")

public class PaypalController {
    @Autowired
    public PaypalService paypalService;
    @Autowired
    private EventServiceImpl eventServiceImpl;
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
        //String returnUrl = "https://govench-api.onrender.com/api/v1/admin/payments/payment";
        String returnUrl = "https://govench-api.onrender.com/api/v1/payments/payment";
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

    @PreAuthorize("hasAnyRole('ORGANIZER','PARTICIPANT')")
    @PostMapping("/subscribe")
    public String  paySubscription() {
        double totalAmount = 50; //La subscripcion valdra 50 solsitos
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        String returnUrl = "https://govench-api.onrender.com/api/v1/payment/subscription?userId="+userId.toString();
        String cancelUrl = "https://blog.fluidui.com/top-404-error-page-examples/";
        User user = userService.getUserbyId(userId);
        try {
            String orderId = paypalService.createOrder(totalAmount, returnUrl, cancelUrl);
            if (orderId == null) {
                //return new RedirectView("/error?status=error");
            }
            if(user.getPremium())
            {
                throw new IllegalArgumentException("Ya eres usuario premium");
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
    @GetMapping("/subscription")
    public String handleSubscription(@RequestParam String token,@RequestParam int userId) {
        try {
            HttpResponse<Order> response = paypalService.captureOrder(token);

            if (response.statusCode() == 201) { // Pago exitoso
                try {
                    userService.SubscribePremium(userId);
                    return "Pago completado con éxito e inscripción realizada.";
                } catch (NotFoundException e) {
                    return "Pago completado, pero hubo un problema con la subscripcion: " + e.getMessage();
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

    @GetMapping("/payment")
    public RedirectView handlePayment(@RequestParam String token, @RequestParam int userId, @RequestParam int eventId) {
        try {
            HttpResponse<Order> response = paypalService.captureOrder(token);

            if (response.statusCode() == 201) { // Pago exitoso

                try {
                    User user = userService.getUserbyId(userId);


                    Event event = eventService.getEventById(eventId);

                    UserEvent createdUserEvent= new UserEvent();
                    createdUserEvent.setId(new IdCompuestoU_E(userId, eventId));
                    createdUserEvent.setUser(user);
                    createdUserEvent.setEvent(event);
                    createdUserEvent.setRegistrationDate(LocalDate.now());
                    createdUserEvent.setNotificationsEnabled(false);
                    createdUserEvent.setFinalReminderSent(false);
                    createdUserEvent.setLastReminderSentDate(null);
                    createdUserEvent.setNotificationsEnabled(false);
                    userEventService.addUserEvent(createdUserEvent);

                    RedirectView redirectView = new RedirectView("https://govench-fb742.web.app/pago/confirmado"); // Ruta de la página de éxito
                    redirectView.addStaticAttribute("message", "Pago completado con éxito e inscripción realizada.");
                    return redirectView;
                } catch (NotFoundException e) {
                    return new RedirectView("/error?message=" + e.getMessage()); // Redirige con error
                } catch (IllegalArgumentException e) {
                    return new RedirectView("/error?message=" + e.getMessage()); // Redirige con error
                }
            } else {
                return new RedirectView("/error?message=El pago fue cancelado o fallido.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new RedirectView("/error?message=Ocurrió un error durante el proceso de pago.");
        }
    }
}

