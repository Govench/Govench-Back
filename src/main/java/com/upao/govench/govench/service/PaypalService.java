package com.upao.govench.govench.service;



import java.io.IOException;

import com.paypal.http.HttpResponse;
import com.paypal.orders.*;

public interface PaypalService {
    public String createOrder(double cost, String returnUrl, String cancelUrl)throws IOException;

    public HttpResponse<Order> captureOrder(String orderId) throws IOException;
    // tipo de retorno de captureORder "com.paypal.http.HttpResponse<Order>"
}