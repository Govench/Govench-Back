package com.upao.govench.govench.api;

import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.upao.govench.govench.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/admin/payments")
public class PaypalController {
    @Autowired
    public PaypalService paypalService;


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
    public String handlePayment(@RequestParam String token) {
        try {

            HttpResponse<Order> response = paypalService.captureOrder(token);

            // Si la captura es exitosa
            if (response.statusCode() == 201) { // Código de estado 201 indica creación exitosa
                //return new RedirectView("/payment-success?status=success"); // Redirigir a página de éxito
                return "Pago completado con éxito.";
            } else {
                // Si la captura falla, redirigir a página de cancelación
                //return new RedirectView("/payment-canceled?status=canceled");
                return "El pago fue cancelado o fallido.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            //return new RedirectView("/error?status=error");
            return "Ocurrió un error durante el proceso de pago.";
        }
    }
}
