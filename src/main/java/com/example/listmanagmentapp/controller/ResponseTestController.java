package com.example.listmanagmentapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseTestController {

    public ResponseTestController() {}

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Połączenie aktywne");
    }
}
