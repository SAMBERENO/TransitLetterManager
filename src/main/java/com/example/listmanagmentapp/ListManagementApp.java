package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.ConfigurationFileReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class ListManagementApp {

    public static void main(String[] args) throws IOException {SpringApplication.run(ListManagementApp.class, args);}

}
