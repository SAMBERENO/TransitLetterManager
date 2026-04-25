package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.controller.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListManagmentAppApplication {

    public static void main(String[] args) {SpringApplication.run(ListManagmentAppApplication.class, args);

        Controller controller = new Controller(new DBConnectionConfig());

        try{
            controller.dodajj("3 test piwka");
            System.out.println(controller.odczyt());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
