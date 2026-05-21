package com.example.listmanagmentapp.service;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImagePreProcessing {

    public ImagePreProcessing() {}

    public void loadImage(String imagePath) {
        Mat source = Imgcodecs.imread(imagePath);
        Mat gray = new Mat();
        Mat blur = new Mat();
        Mat sharpened = new Mat();
        Mat binary = new Mat();
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, blur, new Size(3, 3), 0);
        Core.addWeighted(gray, 1.6, blur, -0.6, 0, sharpened);
        Imgproc.adaptiveThreshold(sharpened, binary, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 41, 32);

        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", binary);


    }
}
