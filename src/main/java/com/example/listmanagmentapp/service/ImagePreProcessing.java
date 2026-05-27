package com.example.listmanagmentapp.service;

import com.sun.tools.javac.Main;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

@Service
public class ImagePreProcessing {

    /*
    *
    *
        Klasa do weryfikacji przy odpowiednim zdjęciu
    *
    *
     */

    public ImagePreProcessing() {}

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
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\3FIN.jpg", binary);
        return binary;
    }

    public Mat morphologyImage(String imagePath){
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 1));
        Mat morphologyImg = new Mat();
        Imgproc.morphologyEx(binaryImage(imagePath), morphologyImg, Imgproc.MORPH_OPEN, kernel);
        return morphologyImg;
    }

    private double detectRotationAngle(Mat binaryImage) {
        Mat lines = new Mat();
        Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, 100);

        double angle = 0;
        System.out.println(lines.rows() + " " + lines.cols());
        //Do debugowania
        Mat debugImage = binaryImage.clone();
        Imgproc.cvtColor(debugImage, debugImage, Imgproc.COLOR_GRAY2BGR);

        for (int x = 0; x < lines.cols(); x++) {
            double[] vec = lines.get(0, x);
            double x1 = vec[0];
            double y1 = vec[1];
            double x2 = vec[2];
            double y2 = vec[3];

            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);

            Imgproc.line(debugImage, start, end, new Scalar(255, 255, 0), 3);

            angle = calculateAngleFromPoints(start, end);
        }

        Imgcodecs.imwrite("detectedLines.jpg", debugImage);
        System.out.println(angle);
        return angle;
    }

    private double calculateAngleFromPoints(Point start, Point end) {
        double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;
        return Math.atan2(deltaY, deltaX) * (180 / Math.PI);
    }

    private Mat rotateImage(Mat image, double angle) {
        Point imgCenter = new Point(image.cols() / 2.0, image.rows() / 2.0);
        Mat rotMtx = Imgproc.getRotationMatrix2D(imgCenter, angle, 1.0);
        Rect bbox = new RotatedRect(imgCenter, image.size(), angle).boundingRect();
        Mat rotatedImage = image.clone();
        Imgproc.warpAffine(image, rotatedImage, rotMtx, bbox.size());

        return rotatedImage;
    }

    public Mat straightenImage(String image) {
        Mat rotatedImage = binaryImage(image);
        Mat processed = morphologyImage(image);
        double rotationAngle = detectRotationAngle(processed);

        return rotateImage(rotatedImage, rotationAngle - 90);
    }

    public void saveImage(String path) {
        Mat image = straightenImage(path);
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", image);
    }
}
