package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.DbRepository;
import com.example.listmanagmentapp.service.ListsCreationOrganizerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import tools.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ListManagementApp {

    public static void main(String[] args) {
        SpringApplication.run(ListManagementApp.class, args);

        //ImagePreProcessingDeWarping imagePreProcessing = new ImagePreProcessingDeWarping();

        //imagePreProcessing.saveImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\croppedTest.jpg");

        //GoogleCloudVisionService andek = new GoogleCloudVisionService();

        //System.out.println(andek.getGoogleVisionResponse(andek.requestGoogleVision()));

        /*TODO: na następny raz:

        - zmienić DbRepository

         */

    }

}
