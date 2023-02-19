package com.scentbird.covidcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CovidCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CovidCaseApplication.class, args);
    }

}
