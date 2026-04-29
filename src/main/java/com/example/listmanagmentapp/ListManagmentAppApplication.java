package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.service.ListCreatingService;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

@SpringBootApplication
public class ListManagmentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListManagmentAppApplication.class, args);

        ListCreatingService listCreatingService = new ListCreatingService(new DBConnectionConfig());

        listCreatingService.createTranzitLetter();
        listCreatingService.createShortagesLetter();
    }

}
