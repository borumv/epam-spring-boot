package com.vlasevsky.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableFeignClients
public class GymAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymAppApplication.class, args);
    }

}
