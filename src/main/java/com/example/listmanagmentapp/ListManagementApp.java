package com.example.listmanagmentapp;

import com.example.listmanagmentapp.service.ImagePreProcessingDeWarping;
import com.example.listmanagmentapp.service.ZXingCodeReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.zxing.Result;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ListManagementApp {

        static void main(String[] args) throws IOException, NotFoundException {SpringApplication.run(ListManagementApp.class, args);

            ZXingCodeReader zxingCodeReader = new ZXingCodeReader();

            List<Result[]> results = zxingCodeReader.decodeImage("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\Kody.pdf");

            for (Result[] result : results) {
                for (Result r : result) {
                    System.out.println(r.toString());
                }
            }

            /*
                ZXingCodeReader reader = new ZXingCodeReader();
                Result[] result = reader.decodeImage("C:/Users/arek4/OneDrive/Pulpit(1)/huj.pdf");

                for(Result r:result){
                        System.out.println(r.getText());
                        //for(ResultPoint rp:r.getResultPoints()){
                          //      System.out.println(rp);
                        //}
                }

             */
        }
}