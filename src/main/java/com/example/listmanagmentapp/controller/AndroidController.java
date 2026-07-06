package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.config.DbRepository;
import com.example.listmanagmentapp.dto.RecordsJson;
import com.example.listmanagmentapp.service.ListsCreationOrganizerService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.Map;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final DbRepository dbRepository;
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ZdjeciaDoSkanowania/";
    private final LocalTime time = LocalTime.now();
    private final ListsCreationOrganizerService listsCreationOrganizerService;

    public AndroidController(DbRepository dbRepository,  ListsCreationOrganizerService listsCreationOrganizerService) {
        this.dbRepository = dbRepository;
        this.listsCreationOrganizerService = listsCreationOrganizerService;
    }

    @GetMapping("readJson")
    public ResponseEntity<?> readJson() {
        try {
            for (RecordsJson recordsJson : dbRepository.readJson()) {
                System.out.println(recordsJson);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Błąd: "  + e.getMessage());
        }
    }

    @GetMapping("/createLists")
    public ResponseEntity<?> createLists() {
        try {
            listsCreationOrganizerService.createLists();
            return ResponseEntity.ok("Utworzono liste");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Błąd: " + e.getMessage());
        }
    }

    @PostMapping("/addJson")
    public ResponseEntity<?> addJson(@RequestBody String json) {
        try {
            dbRepository.addJson(json);
            return ResponseEntity.ok("Dodano Json");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd: "  + e.getMessage());
        }
    }















    /*@PostMapping("/dodajJson")
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
    }*/




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
