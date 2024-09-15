package com.upao.govench.govench;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GovenchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovenchApplication.class, args);
    }

}
