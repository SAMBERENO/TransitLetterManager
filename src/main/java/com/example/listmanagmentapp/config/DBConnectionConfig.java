package com.example.listmanagmentapp.config;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DBConnectionConfig {

    public DBConnectionConfig() {}

    public Connection dbConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:C:/Softs/ProjektDlaStarego/ListManagmentApp/src/main/resources/JsonBuffer");
        }
    }
