package com.example.listmanagmentapp;

import com.example.listmanagmentapp.service.BarcodeReaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(ListManagementApp.class, args);

        BarcodeReaderService barcodeReaderService = new BarcodeReaderService();

        barcodeReaderService.scanBarCodes("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\kodyTest2.png");

        //ImagePreProcessing imagePreProcessing = new ImagePreProcessing();

        //imagePreProcessing.saveImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\pobrane.jpg");

        //ImagePreProcessingDeWarping imagePreProcessingDeWarping = new ImagePreProcessingDeWarping();

        //imagePreProcessingDeWarping.saveImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\pobrane1.jpg");
    }

}
