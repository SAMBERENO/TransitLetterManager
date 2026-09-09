package com.example.listmanagmentapp;

import com.example.listmanagmentapp.service.ZXingCodeReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.zxing.Result;
import java.io.IOException;

@SpringBootApplication
public class ListManagementApp {

        static void main(String[] args) throws NotFoundException, IOException {SpringApplication.run(ListManagementApp.class, args);

                ZXingCodeReader reader = new ZXingCodeReader();
                Result[] result = reader.decodeImage("C:/Users/arek4/OneDrive/Pulpit(1)/huj.pdf");

                for(Result r:result){
                        System.out.println(r.getText());
                        //for(ResultPoint rp:r.getResultPoints()){
                          //      System.out.println(rp);
                        //}
                }
        }
}