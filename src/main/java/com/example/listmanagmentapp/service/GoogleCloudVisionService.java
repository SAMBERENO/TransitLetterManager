package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.controller.AndroidController;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Scanner;

@Service
public class GoogleCloudVisionService {

    private final String ApiKeyPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/APIKlucz.txt";
    private final String targetUrl = "https://vision.googleapis.com/v1/images:annotate?key=";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ZdjeciaDoSkanowania/";
    private final ImagePreProcessing imagePreProcessing = new ImagePreProcessing();
    private final AndroidController androidController = new AndroidController(imagePreProcessing, this);

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
            return null;
        }
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
            return new RuntimeException(e).getMessage();
        }
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

}
