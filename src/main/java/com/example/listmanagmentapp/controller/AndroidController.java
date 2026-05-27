package com.example.listmanagmentapp.controller;

import com.example.listmanagmentapp.service.GoogleCloudVisionService;
import com.example.listmanagmentapp.service.ImagePreProcessing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ZdjeciaDoSkanowania/";
    private final LocalTime time = LocalTime.now();
    private final ImagePreProcessing imagePreProcessing;
    private final GoogleCloudVisionService gcvs;

    public AndroidController(ImagePreProcessing imagePreProcessing, GoogleCloudVisionService gcvs) {
        this.imagePreProcessing = imagePreProcessing;
        this.gcvs = gcvs;
    }

    @PostMapping("/essunia")
    public ResponseEntity<?> essunia(@RequestParam("file") MultipartFile file) {
        try{
            imagePreProcessing.straightenImage(addImage(file).getBody());
            gcvs.getGoogleVisionResponse(gcvs.requestGoogleVision());

            //TODO: Dokończyć z odpowiednim zdjęciem

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
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
