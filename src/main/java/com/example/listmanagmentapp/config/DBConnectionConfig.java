package com.example.listmanagmentapp.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DBConnectionConfig {

    private Connection connection;

    public DBConnectionConfig() {}

    @Bean
    public Connection dbConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Softs/ProjektDlaStarego/ListManagmentApp/src/main/resources/JsonBuffer");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
