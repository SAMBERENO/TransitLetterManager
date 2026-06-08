package com.example.listmanagmentapp.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImagePreProcessingDeWarping {

    public ImagePreProcessingDeWarping() {}

    private Mat returnImage(String imagePath){
        return Imgcodecs.imread(imagePath);
    }

    public Mat bilateralFilter(String imagePath){
        Mat image = returnImage(imagePath);
        Mat bilateral = new Mat();
        Imgproc.bilateralFilter(image, bilateral, 20, 100, 100);
        return bilateral;
    }

    public Mat nlm(String imagePath){
        Mat image = bilateralFilter(imagePath);
        Mat nlm = new Mat();
        Photo.fastNlMeansDenoising(image, nlm, 10, 31, 51);
        return nlm;
    }

    public Mat grayImage(String imagePath){
        Mat image = nlm(imagePath);
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    public Mat binaryImage(String imagePath){
        Mat image = grayImage(imagePath);
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(image, binary, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 31, 21); //41, 31 najlepsze wartosci
        return binary;
    }

    public Mat medianBlur(String imagePath){
        Mat image = binaryImage(imagePath);
        Mat median = new Mat();
        Imgproc.medianBlur(image, median, 5);
        Imgcodecs.imwrite("medianblur.jpg", median);
        return median;
    }

    public List<MatOfPoint> findContours(String imagePath) {
        Mat source = medianBlur(imagePath);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(source, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public Mat verticalLinesRemoval(String imagePath){
        Mat medianImage = medianBlur(imagePath);
        Mat verticalKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 40));
        Mat verticalMorphology = new Mat();
        Mat cleanedImage = new Mat();

        Imgproc.morphologyEx(medianImage, verticalMorphology, Imgproc.MORPH_CLOSE, verticalKernel);
        Imgproc.morphologyEx(verticalMorphology, verticalMorphology, Imgproc.MORPH_DILATE, verticalKernel);

        Photo.inpaint(medianImage, verticalMorphology, cleanedImage, 1, Photo.INPAINT_TELEA);
        return cleanedImage;
    }

    public Mat morphologyDilation(String imagePath){
        Mat morphologyImage = verticalLinesRemoval(imagePath);
        List<MatOfPoint> contours = findContours(imagePath);
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            if (rect.width < 500 && rect.height < 150 && rect.width > 20 && rect.height > 10) Imgproc.rectangle(morphologyImage, rect.tl(), rect.br(), new Scalar(255, 0, 0), -1);
        }
        Imgcodecs.imwrite("morphed.jpg", morphologyImage);
        return morphologyImage;
    }

    public void saveImage(String imagePath) {
        Mat image = morphologyDilation(imagePath);
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", image);
    }

    /* TODO:
    Do zrobienia spanów:
1. --findContours / connectedComponents
2. --boundingRect dla każdego elementu
3. policz odległości między elementami
4. połącz bliskie elementy
5. utwórz grupę (span)
6. utwórz jeden duży bounding box dla grupy
     */

}
