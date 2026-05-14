package com.example.listmanagmentapp.controller;

import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalTime;
import java.util.Base64;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final String ApiKeyPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/APIKlucz.txt";
    private MultipartFile image = null;
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/Zdjęcia do skanowania/";
    private final LocalTime time = LocalTime.now();

    private final RestClient restClient;

    public AndroidController(RestClient restClient) {
        this.restClient = restClient;
    }

    public BatchAnnotateImagesResponse requestGoogle(){

        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){

            byte[] imageBytes = FileUtils.readFileToByteArray(new File(image.getOriginalFilename()));
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            return restClient
                    .post()
                    .uri("https://vision.googleapis.com/v1/images:" + encodedImage + "?" + ApiKeyPath)
                    .


        } catch (FileNotFoundException e){
            System.out.println("Nie znaleziono pliku APIKey.txt " + e.getMessage());
        } catch (IOException e){
            System.out.println("Blad IO: " + e.getMessage());
        }

    }



    @GetMapping("/getKlucz")
    public String getKlucz(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            return new String(fin.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/dodajZdjecie")
    public ResponseEntity addImage(@RequestParam("file") MultipartFile file) {
        try {

            file.transferTo(new File(outputPath + time + ".jpg"));
            image = file;

            ResponseEntity.ok();
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono zdjęcia: " + e.getMessage());
        } catch (BadRequestException e) {
            ResponseEntity.badRequest();
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }
        return null;
    }
}
