package com.example.listmanagmentapp;

import com.example.listmanagmentapp.controller.AndroidController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(ListManagementApp.class, args);

        AndroidController andek = new AndroidController();

        System.out.println(andek.getGoogleVisionResponse(andek.requestGoogleVision()));

        //TODO: Zmienić argument w AndroidController.getEncodedImage() na return of addImage

    }

}
