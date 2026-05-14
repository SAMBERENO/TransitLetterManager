package com.example.listmanagmentapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalTime;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final String ApiKeyPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/APIKlucz.txt";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/Zdjęcia do skanowania/";
    private final LocalTime time = LocalTime.now();

    public AndroidController() {}

    @GetMapping("/getKlucz")
    public String getKlucz(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            return new String(fin.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/dodajZdjecie")
    public MultipartFile addImage(@RequestParam("file") MultipartFile file) {
        try (FileInputStream fin = new FileInputStream(ApiKeyPath);
             FileOutputStream fout = new FileOutputStream(outputPath + time + ".jpg")) {


            fout.write(file.getBytes());

            return file;
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono zdjęcia: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }
        return file;
    }
}
