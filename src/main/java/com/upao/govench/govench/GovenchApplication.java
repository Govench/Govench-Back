package com.upao.govench.govench;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GovenchApplication {

    public static void main(String[] args) {
        System.setProperty("PAYPAL_CLIENT_ID", "Acu7Ifuu8njmFqnr0Jyu6cFUBR4jtSw-4NRqYJHPAxOqEw-ky_5G1l_vEStWDwlc6nAx_1A1dlTEmx_Z");
        System.setProperty("PAYPAL_CLIENT_SECRET", "EIlCii_R_Albk9ktXSFfRQ6FRUtfjhiAq-VskUQzyS8y1RbKE4741_VvawMySppmjDL4diecaCRwbGlG");
        SpringApplication.run(GovenchApplication.class, args);
    }

}
