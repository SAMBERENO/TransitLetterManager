package com.example.listmanagmentapp.controller;

import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.time.LocalTime;
import java.util.Base64;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final String ApiKeyPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/APIKlucz.txt";
    private File image = null;
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/Zdjęcia do skanowania/";
    private final LocalTime time = LocalTime.now();
    private final String targetUrl = "https://vision.googleapis.com/v1/images:annotate?key=";

    private final RestClient restClient;

    public AndroidController(RestClient restClient) {
        this.restClient = restClient;
    }



    public BatchAnnotateImagesResponse requestGoogle(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            URLConnection urlConnection = serverUri().toURL().openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);

            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            //TODO: Potwierdzić czy kolejność "features" -> "image" jest poprawna czy na odwrót
            httpRequestBodyWriter.write("{\"requests\":  " +
                    "[{ \"features\":  " +
                    "[{\"type\": \"LABEL_DETECTION\" }], " +
                    //TODO: poniżej dodać encodedImage
                    "\"image\": {\"source\": { \"gcsImageUri\":\"gs://vision-sample-images/4_Kittens.jpg\"}}}]}");
            httpRequestBodyWriter.close();;

            String response = httpURLConnection.getResponseMessage();

            if(httpURLConnection.getInputStream() == null){
                System.out.println("Brak stream");
                return null;
            }

            //TODO: Kontynuować pisanie kodu

            //Te 2 linijki poniżej są na pewno dobrze :3
            byte[] imageBytes = FileUtils.readFileToByteArray(image);
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);


        } catch (FileNotFoundException e){
            System.out.println("Nie znaleziono pliku APIKey.txt " + e.getMessage());
        } catch (IOException e){
            System.out.println("Blad IO: " + e.getMessage());
        }
        return null;
    }

    public String getKlucz(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            return new String(fin.readAllBytes());
        } catch (IOException e) {
            return new RuntimeException(e).getMessage();
        }
    }

    public URI serverUri(){
        try {
            return new URI(targetUrl + getKlucz());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEncodedImage(){
        try {
            byte[] imageBytes = FileUtils.readFileToByteArray(image);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return new RuntimeException(e).getMessage();
        }
    }

    public String getImagePath(){
        return outputPath + time + ".jpg";
    }

    @PostMapping("/dodajZdjecie")
    public ResponseEntity addImage(@RequestParam("file") MultipartFile file) {
        try {
            File imageFile = new File(getImagePath());
            file.transferTo(imageFile);
            image = imageFile;

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
