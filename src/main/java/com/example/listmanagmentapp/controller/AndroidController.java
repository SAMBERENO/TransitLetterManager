package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.config.DbRepository;
import com.example.listmanagmentapp.dto.RecordsJson;
import com.example.listmanagmentapp.service.ListsCreationOrganizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final DbRepository dbRepository;
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

    @GetMapping("/getJsonByID")
    public String getJsonByID(@RequestParam String nrZleceniaiPudla) {
        try {
            return dbRepository.getJsonByID(nrZleceniaiPudla);
        } catch (Exception e) {
            return "Błąd: " + e.getMessage();
        }
    }

    @GetMapping("/createLists")
    public ResponseEntity<?> createLists() {
        try {
            if (dbRepository.dbRecordsCount() > 0) {
                listsCreationOrganizerService.createLists();
                dbRepository.deleteZatwierdzone();
                return ResponseEntity.ok("Utworzono liste");
            }
            else
                return ResponseEntity.ok("Brak pozycji w bazie danych!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Błąd: " + e.getMessage());
        }
    }

    @PostMapping("addJsonFromAndroid")
    public ResponseEntity<?> addJsonFromAndroid(@RequestBody String json) {
        try {
            dbRepository.addJsonFromAndroid(json);
            return ResponseEntity.ok("Dodano pozycje");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd: "  + e.getMessage());
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

    @PutMapping("/updateRecord")
    public ResponseEntity<?> updateRecord(@RequestBody String json) {
        try {
            dbRepository.updateRecord(json);
            if (dbRepository.dbRecordsCount() >= 13)
                listsCreationOrganizerService.createShortagesList();
            return ResponseEntity.ok("Zaktualizowano pozycje");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd: " + e.getMessage());
        }
    }

    @DeleteMapping("/usun")
    public ResponseEntity<?> deleteAll() {
        try {
            dbRepository.deleteAll();
            return ResponseEntity.ok("Usunięto wszystkie wpisy");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd: " + e.getMessage());
        }
    }

    @DeleteMapping("/usun/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        try {
            dbRepository.deleteOne(id);
            return ResponseEntity.ok("Usunięto wpis");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd: " + e.getMessage());
        }
    }
}
