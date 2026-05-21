package com.example.listmanagmentapp.controller;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Scanner;

@RequestMapping("/android")
@RestController
public class AndroidController {

    private final String ApiKeyPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/APIKlucz.txt";
    private File image = null;
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ZdjeciaDoSkanowania/";
    private final LocalTime time = LocalTime.now();
    private final String targetUrl = "https://vision.googleapis.com/v1/images:annotate?key=";

    public HttpURLConnection requestGoogleVision(){
        try{
            URL serverUrl = new URL(getServerUri().toString());
            URLConnection urlConnection = serverUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            httpRequestBodyWriter.write("{\"requests\":  " +
                    "[{ \"features\":  " +
                    "[{\"type\": \"DOCUMENT_TEXT_DETECTION\" }], " +
                    "\"image\": {\"content\":\"" + getEncodedImage() +  "\"}}]}");
            httpRequestBodyWriter.close();
            return httpURLConnection;
        } catch (IOException e) {
            System.out.println("Blad IO: " + e.getMessage());
        }
        return null;
    }

    public String getGoogleVisionResponse(HttpURLConnection httpURLConnection){
        try{
            if(httpURLConnection.getInputStream() == null){
                System.out.println("Brak stream");
                return null;
            }
            Scanner httpResponseBodyScanner = new Scanner(httpURLConnection.getInputStream());
            String response = "";
            while (httpResponseBodyScanner.hasNext()){
                String line = httpResponseBodyScanner.nextLine();
                response += line;
            }
            httpResponseBodyScanner.close();
            return response;
        } catch (IOException e) {
            System.out.println("Blad IO: " + e.getMessage());
        }
        return null;
    }

    public AnnotateImageResponse requestGoogle(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            URLConnection urlConnection = getServerUri().openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);

            //Te 2 linijki poniżej są na pewno dobrze :3
            byte[] imageBytes = FileUtils.readFileToByteArray(image);
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            httpRequestBodyWriter.write("{\"requests\":  " +
                    "[{ \"features\":  " +
                    "[{\"type\": \"DOCUMENT_TEXT_DETECTION\" }], " +
                    "\"image\": {\"content\":\"" + encodedImage +  "\"}}]}");
            httpRequestBodyWriter.close();

            String response = httpURLConnection.getResponseMessage();

            if(httpURLConnection.getInputStream() == null){
                System.out.println("Brak stream");
                return null;
            }

            //TODO: Kontynuować pisanie kodu


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

    public URL getServerUri(){
        try {
            return new URL(targetUrl + getKlucz());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEncodedImage(){
        try {
            //TODO: Zamienić argument "image" na zmienioną wersję addImage()
            byte[] imageBytes = FileUtils.readFileToByteArray(new File(outputPath + "1.jpg"));
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
