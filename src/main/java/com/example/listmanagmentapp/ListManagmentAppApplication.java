package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.service.ListsCreationOrganizerService;
import com.example.listmanagmentapp.service.RecordsFetchService;
import com.example.listmanagmentapp.service.ShortagesLetterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ListManagmentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListManagmentAppApplication.class, args);

        ListsCreationOrganizerService listsCreationOrganizerService = new ListsCreationOrganizerService(new DBConnectionConfig(), new ObjectMapper());
        listsCreationOrganizerService.createLists();

    }

}
