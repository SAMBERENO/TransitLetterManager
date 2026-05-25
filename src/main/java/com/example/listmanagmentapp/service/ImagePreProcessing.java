package com.example.listmanagmentapp.service;

import com.sun.tools.javac.Main;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
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

    public Mat loadImage(String imagePath) {
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
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20, 5));
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_OPEN, kernel);
        return binary;
    }

    private double detectRotationAngle(Mat binaryImage) {
        Mat lines = new Mat();
        Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, 100);

        double angle = 0;

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

            Imgproc.line(debugImage, start, end, new Scalar(255, 255, 0), 5);

            angle = calculateAngleFromPoints(start, end);
        }

        Imgcodecs.imwrite("detectedLines.jpg", debugImage);

        return angle;
    }

    private double calculateAngleFromPoints(Point start, Point end) {
        double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;
        return Math.atan2(deltaY, deltaX) * (180 / Math.PI);
    }

    private Mat rotateImage(Mat image, double angle) {
        Point imgCenter = new Point(image.cols() / 2, image.rows() / 2);
        Mat rotMtx = Imgproc.getRotationMatrix2D(imgCenter, angle, 1.0);
        Rect bbox = new RotatedRect(imgCenter, image.size(), angle).boundingRect();

        Mat rotatedImage = image.clone();
        Imgproc.warpAffine(image, rotatedImage, rotMtx, bbox.size());

        return rotatedImage;
    }

    public Mat straightenImage(String image) {
        Mat rotatedImage = Imgcodecs.imread(image);
        Mat processed = loadImage(image);
        double rotationAngle = detectRotationAngle(processed);

        return rotateImage(rotatedImage, rotationAngle);
    }
}
