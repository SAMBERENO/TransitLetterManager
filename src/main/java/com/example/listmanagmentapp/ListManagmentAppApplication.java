package com.example.listmanagmentapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class ListManagmentAppApplication {

    public static void main(String[] args) {

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Softs/ProjektDlaStarego/ListManagmentApp/src/main/resources/JsonBuffer");
        Statement statement = connection.createStatement();) {

            statement.setQueryTimeout(15);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Tabela (id INTEGER PRIMARY KEY, słowo TEXT, imie TEXT)");

            statement.executeUpdate("INSERT INTO Tabela (id, słowo, imie) VALUES (1, 'essa', 'Johny')");

            ResultSet rs = statement.executeQuery("select * from Tabela");
            while(rs.next()){
                System.out.println(rs.getInt("id") + " " + rs.getString("słowo") + " " + rs.getString("imie"));
            }


        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        SpringApplication.run(ListManagmentAppApplication.class, args);
    }

}
