package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.config.DbRepository;
import com.example.listmanagmentapp.dto.RecordsJson;
import com.example.listmanagmentapp.service.ListsCreationOrganizerService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalTime;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final DbRepository dbRepository = new DbRepository();
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ZdjeciaDoSkanowania/";
    private final LocalTime time = LocalTime.now();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ListsCreationOrganizerService listsCreationOrganizerService = new ListsCreationOrganizerService();

    public AndroidController() {}

    @PostMapping("/dodajJson")
    public ResponseEntity<?> addJson(@RequestBody String json) {
        try(Connection connection = dbRepository.dbConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO DaneJson (json) VALUES (?)")) {
            ps.setQueryTimeout(10);
            objectMapper.readValue(json, RecordsJson.class);
            ps.setString(1, json);
            ps.executeUpdate();
            return ResponseEntity.ok("Dodano");
        } catch (JsonMappingException | JsonParseException e) {
            return ResponseEntity.badRequest().body("Blad z walidacją Json'a: " + e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Blad: " + e.getMessage());
        }
    }




    //@PostMapping("/dodajZdjecie")
    public ResponseEntity<String> addImage(@RequestParam("file") MultipartFile file) {
        try {
            File imageFile = new File(getImagePath());
            file.transferTo(imageFile);
            return ResponseEntity.ok(imageFile.getAbsolutePath());
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Zły stan aplikacji: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Blad zapisu pliku: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Blad: " + e.getMessage());
        }
    }

    public String getImagePath(){
        return outputPath + time + ".jpg";
    }

}
