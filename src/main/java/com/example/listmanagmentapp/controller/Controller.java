package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/controller")
@RestController
public class Controller {

    /*
    TODO: Utworzyć po skończeniu warstwy serwisowej:
        - Usunąć Query z metod
        - Ogólna redukcja/optymalizacja kodu
        - Zamienić void na ResponseEntity
        - Poprawić obsługę błędów
        - Kontroler odpowiedzialny za generowanie plików excel
        - Kontroler do live testowania działania aplikacji(połączeń i stanu)
     */

    private final DBConnectionConfig dbConnectionConfig;
    private final ObjectMapper objectMapper;


    public Controller(DBConnectionConfig dbConnectionConfig, ObjectMapper objectMapper) {
        this.dbConnectionConfig = dbConnectionConfig;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/odczyt")
    public Map<Integer, String> read() {
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
        }
        return list;
    }

    //TODO: ustawić zabezpieczenia przed niechcianym usunięciem plików przez kogoś nieproszonego
    @DeleteMapping("/usun")
    public ResponseEntity<?> deleteAll(){
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement()){
            statement.setQueryTimeout(10);
            connection.setAutoCommit(false);
            try {
                statement.executeUpdate("DELETE FROM DaneJson");
                statement.executeUpdate("Delete from sqlite_sequence where name = 'DaneJson'");
                connection.commit();
                return ResponseEntity.ok("Usunięto wszystkie wpisy");
            } catch (SQLException e) {
                connection.rollback();
                return ResponseEntity.badRequest().body("Blad: " + e.getMessage());            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Blad: " + e.getMessage());
        }
    }

    //TODO: ustawić zabezpieczenia niedopuszczające osób trzecich do dodawania pozycji
    @PostMapping("/dodajJson")
    public void add(@RequestBody String json) {
        addSafeQuery(json);
    }

    //TODO: ustawić zabezpieczenia przed niechcianym usunięciem plików przez kogoś nieproszonego
    @DeleteMapping("/usun/{wpis}")
    public void delete(@PathVariable int wpis){
            deleteSafeQuery(wpis);
    }

    private ResponseEntity<?> deleteSafeQuery(int wpis) {
        try(Connection connection = dbConnectionConfig.dbConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM DaneJson WHERE id = ?")) {
            ps.setQueryTimeout(10);
            ps.setInt(1, wpis);
            if(ps.executeUpdate() == 0){
                System.out.println("Nie znaleziono wpisu");
            }
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Blad: " + e.getMessage());
        }
    }

    private ResponseEntity<?> addSafeQuery(String json) {
        try(Connection connection = dbConnectionConfig.dbConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO DaneJson (json) VALUES (?)")){
            ps.setQueryTimeout(10);
            objectMapper.readTree(json);
            ps.setString(1, json);
            ps.executeUpdate();
            return ResponseEntity.ok("OK");
        }  catch (Exception e){
            return ResponseEntity.badRequest().body("Blad: " + e.getMessage());
        }
    }
}
