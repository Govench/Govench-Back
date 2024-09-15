package com.upao.govench.govench;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GovenchApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // Asignar las variables de entorno a las propiedades del sistema
        System.setProperty("PAYPAL_CLIENT_ID", dotenv.get("PAYPAL_CLIENT_ID"));
        System.setProperty("PAYPAL_CLIENT_SECRET", dotenv.get("PAYPAL_CLIENT_SECRET"));
        SpringApplication.run(GovenchApplication.class, args);


    }

}
