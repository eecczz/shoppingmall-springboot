package com.example.paymentapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @GetMapping("/api/data")
    public String getData() {
        return "{\"message\": \"React와 Spring Boot 연동 성공!\"}";
    }
}
