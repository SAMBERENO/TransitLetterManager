package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.service.ListsCreationOrganizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/excel")
@RestController
public class ExcelController {

    private final ListsCreationOrganizerService listsCreationOrganizerService;

    public ExcelController(ListsCreationOrganizerService listsCreationOrganizerService) {
        this.listsCreationOrganizerService = listsCreationOrganizerService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createLists(){
        try {
            listsCreationOrganizerService.createLists();
            return ResponseEntity.ok("Excels created");
        } catch (ResponseStatusException e){
            System.out.println("ResponseException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Excels not created");
    }
}
