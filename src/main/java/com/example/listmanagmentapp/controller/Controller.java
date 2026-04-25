package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.helpmethods.IdManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private final DBConnectionConfig dbConnectionConfig;
    private final IdManager idManager = new IdManager();

    public Controller(DBConnectionConfig dbConnectionConfig) {
        this.dbConnectionConfig = dbConnectionConfig;
    }

    @GetMapping("/odczyt")
    public Map<Integer, String> odczyt() {
        Map<Integer, String> list = new HashMap<>();
        try(Connection connection = dbConnectionConfig.dbConnection()){
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);
            ResultSet rs = statement.executeQuery("SELECT * FROM DaneJson");
            while(rs.next()){
                list.put(rs.getInt(1), rs.getString(2));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return list;
    }

    @PostMapping("/dodajj")
    public void dodajj(String json) {
        try(Connection connection = dbConnectionConfig.dbConnection()){
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO DaneJson (id, json) VALUES (?, ?)");
            ps.setInt(1, idManager.provideId());
            ps.setString(2, json);
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/dodaj")
    public void dodaj() {
        try (Connection connection = dbConnectionConfig.dbConnection()){
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeQuery("INSERT INTO Tabela (id, słowo, imie) VALUES (3, słowo, Damian)");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
