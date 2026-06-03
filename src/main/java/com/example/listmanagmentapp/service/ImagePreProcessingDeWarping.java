package com.example.listmanagmentapp.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImagePreProcessingDeWarping {

    public ImagePreProcessingDeWarping() {}

    private Mat returnImage(String imagePath){
        return Imgcodecs.imread(imagePath);
    }

    public Mat grayImage(String imagePath){
        Mat gray = new Mat();
        Imgproc.cvtColor(returnImage(imagePath), gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    public Mat claheImage(String imagePath){
        Mat claheImg = new Mat();
        CLAHE clahe = Imgproc.createCLAHE(2.0, new Size(8, 8));
        clahe.apply(grayImage(imagePath), claheImg);
        return claheImg;
    }

    public Mat blurImage(String imagePath){
        Mat blur = new Mat();
        Imgproc.GaussianBlur(claheImage(imagePath), blur, new Size(3, 3), 0);
        return blur;
    }

    public Mat sharpenImage(String imagePath){
        Mat sharpened = new Mat();
        Core.addWeighted(grayImage(imagePath), 1.6, blurImage(imagePath), -0.6, 0, sharpened);
        return sharpened;
    }

    public Mat binaryImage(String imagePath){
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(sharpenImage(imagePath), binary, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 31, 11);
        return binary;
    }

    public Mat morphologyImage(String imagePath){
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 1));
        Mat morphologyImg = new Mat();
        Imgproc.morphologyEx(binaryImage(imagePath), morphologyImg, Imgproc.MORPH_OPEN, kernel);
        return morphologyImg;
    }

    public void saveImage(String imagePath) {
        Mat image = binaryImage(imagePath);
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", image);
    }

}
