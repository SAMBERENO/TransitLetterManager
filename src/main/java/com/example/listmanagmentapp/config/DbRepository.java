package com.example.listmanagmentapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Repository
public class DbRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DbRepository() {}

    //Usunąć wszystko poniżej
    private String url = "jdbc:sqlite:C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ZdjeciaDoSkanowania/db.sqlite";

    public Connection dbConnection() throws SQLException {
        return DriverManager.getConnection(url);
        }
    }
