package com.example.listmanagmentapp.config;

import jakarta.annotation.PostConstruct;
import nu.pattern.OpenCV;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCVConfig {

    @PostConstruct
    public void initOpenCV() {
        try {
            OpenCV.loadLocally();
            System.out.println("OpenCV loaded successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
