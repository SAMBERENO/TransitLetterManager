package com.example.listmanagmentapp;

import com.example.listmanagmentapp.service.ImagePreProcessingDeWarping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(ListManagementApp.class, args);

        //ImagePreProcessing imagePreProcessing = new ImagePreProcessing();

        ImagePreProcessingDeWarping imagePreProcessing = new ImagePreProcessingDeWarping();

        imagePreProcessing.saveImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\bigger2.jpg");

        //GoogleCloudVisionService andek = new GoogleCloudVisionService();

        //System.out.println(andek.getGoogleVisionResponse(andek.requestGoogleVision()));

        //TODO: Zmienić argument w AndroidController.getEncodedImage() na return of addImage

    }

}
