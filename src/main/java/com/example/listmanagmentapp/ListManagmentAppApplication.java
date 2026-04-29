package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.service.ListCreatingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListManagmentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListManagmentAppApplication.class, args);

        ListCreatingService listCreatingService = new ListCreatingService(new DBConnectionConfig());

        listCreatingService.createTranzitLetter();
        listCreatingService.createShortagesLetter();
    }

}
