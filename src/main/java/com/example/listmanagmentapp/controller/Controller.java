package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private final DBConnectionConfig dbConnectionConfig;

    public Controller(DBConnectionConfig dbConnectionConfig) {
        this.dbConnectionConfig = dbConnectionConfig;
    }

    @GetMapping("/odczyt")
    public Map<Integer, String> odczyt() {
        Map<Integer, String> list = new HashMap<>();
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM DaneJson")){
            statement.setQueryTimeout(10);
            while(rs.next()){
                list.put(rs.getInt(1), rs.getString(2));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return list;
    }

    //TODO: ustawić zabezpieczenia niedopuszczające osób trzecich do dodawania pozycji
    @PostMapping("/dodaj/{json}")
    public void dodaj(@PathVariable String json) {
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO DaneJson (json) VALUES (?)")){
            statement.setQueryTimeout(10);
            ps.setString(1, json);
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //TODO: ustawić zabezpieczenia przed niechcianym usunięciem plików przez kogoś nieproszonego
    @DeleteMapping("/usun/{wpis}")
    public void usun(@PathVariable int wpis){
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM DaneJson WHERE id = ?")){
            statement.setQueryTimeout(10);
            ps.setInt(1, wpis);
            if(ps.executeUpdate() == 0){
                System.out.println("Nie znaleziono wpisu");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //TODO: ustawić zabezpieczenia przed niechcianym usunięciem plików przez kogoś nieproszonego
    @DeleteMapping("/usun")
    public void usunwszystko(){
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement()){
            statement.setQueryTimeout(10);
            connection.setAutoCommit(false);
            try {
                statement.executeUpdate("DELETE FROM DaneJson");
                statement.executeUpdate("Delete from sqlite_sequence where name = 'DaneJson'");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
