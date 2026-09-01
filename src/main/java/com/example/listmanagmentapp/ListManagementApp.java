package com.example.listmanagmentapp;

import com.example.listmanagmentapp.service.ImagePreProcessing;
import com.example.listmanagmentapp.service.ImagePreProcessingDeWarping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(ListManagementApp.class, args);

        //ImagePreProcessing imagePreProcessing = new ImagePreProcessing();

        //imagePreProcessing.saveImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\pobrane.jpg");

        ImagePreProcessingDeWarping imagePreProcessingDeWarping = new ImagePreProcessingDeWarping();

        imagePreProcessingDeWarping.saveImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\pobrane1.jpg");
    }

}
