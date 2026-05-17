package com.example.listmanagmentapp.controller;

import com.google.cloud.vision.v1.*;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequestMapping("/android")
@RestController
//TODO: Zmienić nzawę i całą klasę do obsługi Google Vision API
public class AndroidController {

    private final String ApiKeyPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/APIKlucz.txt";
    private MultipartFile image = null;
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/Zdjęcia do skanowania/";
    private final LocalTime time = LocalTime.now();
    private final String targetUrl = "https://vision.googleapis.com/v1/images:annotate?key=";

    private final RestClient restClient;

    public AndroidController(RestClient restClient) {
        this.restClient = restClient;
    }

    public void requestGoogleVision(){
        try(ImageAnnotatorClient imageAnnotatorClient = ImageAnnotatorClient.create()){
            BatchAnnotateImagesRequest request = BatchAnnotateImagesRequest.newBuilder()
                    .addAllRequests(new ArrayList<AnnotateImageRequest>())
                    .build();
            BatchAnnotateImagesResponse response = imageAnnotatorClient.batchAnnotateImages(request);





            URLConnection urlConnection = serverUri().toURL().openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //Te 2 linijki poniżej są na pewno dobrze :3
            byte[] imageBytes = FileUtils.readFileToByteArray(new File(image.getOriginalFilename()));
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        } catch (IOException e) {
            System.out.println("Blad IO: " + e.getMessage());
        }

    }

    public AnnotateImageResponse requestGoogle(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            URLConnection urlConnection = serverUri().toURL().openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.setDoOutput(true);

            //Te 2 linijki poniżej są na pewno dobrze :3
            byte[] imageBytes = FileUtils.readFileToByteArray(new File(image.getOriginalFilename()));
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

    public

    public String getKlucz(){
        try(FileInputStream fin = new FileInputStream(ApiKeyPath)){
            return new String(fin.readAllBytes());
        } catch (IOException e) {
            return new RuntimeException(e).getMessage();
        }
    }

    public URI serverUri(){
        try{
            return new URI(targetUrl + getKlucz());
        } catch (URISyntaxException e) {
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
