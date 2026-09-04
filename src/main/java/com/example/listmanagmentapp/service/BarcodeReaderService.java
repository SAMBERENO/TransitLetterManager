package com.example.listmanagmentapp.service;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.BarcodeDetector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarcodeReaderService {

    ImagePreProcessing imagePreProcessing = new ImagePreProcessing();

    public List<String> scanBarCodes(String imagePath){
        List<String> result = new ArrayList<>();
        Mat mat = Imgcodecs.imread(imagePath);
        BarcodeDetector graphicalCodeDetector = new BarcodeDetector();
        graphicalCodeDetector.detectAndDecodeMulti(mat, result);
        System.out.println(result);
        return result;
    }
}
