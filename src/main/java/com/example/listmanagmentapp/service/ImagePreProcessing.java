package com.example.listmanagmentapp.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImagePreProcessing {

    public ImagePreProcessing() {}

    private Mat returnImage(String imagePat) {
        return Imgcodecs.imread(imagePat);
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
        Imgproc.adaptiveThreshold(image, binary, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 21, 11);
        return binary;
    }

    public Mat morphologyImage(String imagePath){
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(500, 3));
        Mat morphologyImg = new Mat();
        Imgproc.morphologyEx(binaryImage(imagePath), morphologyImg, Imgproc.MORPH_OPEN, kernel);
        return morphologyImg;
    }

    private double detectRotationAngle(Mat binaryImage) {
        Mat lines = new Mat();
        Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, 100);

        double angle = 0;
        System.out.println(lines.rows() + " " + lines.cols());
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

    public Mat straightenImage(String imagePath) {
        Mat straightenImage = binaryImage(imagePath);
        Mat processed = morphologyImage(imagePath);
        double rotationAngle = detectRotationAngle(processed);
        return rotateImage(straightenImage, rotationAngle);
    }

    public List<MatOfPoint> findContours(Mat inputMat) {
        Mat source = inputMat.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(source, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public List<Point> approxPolyDP(String imagePath) {
        List<MatOfPoint> contours = findContours(straightenImage(imagePath));
        List<Point> pointList = new ArrayList<>();

        MatOfPoint2f bestApprox = null;
        double bestArea = 0;

        for (MatOfPoint contour : contours) {
            MatOfPoint2f curve = new MatOfPoint2f(contour.toArray());
            double perimeter = Imgproc.arcLength(curve, true);
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            Imgproc.approxPolyDP(curve, approxCurve, perimeter * 0.02, true);

            double area = Imgproc.contourArea(approxCurve);
            if (approxCurve.total() == 4 && area > bestArea) {
                bestArea = area;
                bestApprox = approxCurve;
                System.out.println("Punkty: " + approxCurve.total() + " Area: " + area);
            }
        }
        for (Point p : bestApprox.toArray()) {
            pointList.add(p);
            System.out.println("Punkt: " + p);
        }
        System.out.println("BestApprox: " + bestApprox);
        System.out.println("BestArea: " + bestArea);
        System.out.println("Rozmiar listy punktow: " + pointList.size());
        return pointList;
    }


    public Mat getPerspectiveTransform(String imagePath) {
        Mat depth = returnImage(imagePath);
        List<Point> pointList = approxPolyDP(imagePath);
        System.out.println(pointList.size());
        MatOfPoint2f src = new MatOfPoint2f(pointList.get(3), pointList.get(2), pointList.get(1), pointList.get(0));
        MatOfPoint2f dst = new MatOfPoint2f(new Point(0, 0), new Point(depth.cols(), 0), new Point(depth.cols(), depth.rows()), new Point(0, depth.rows()));
        return Imgproc.getPerspectiveTransform(src, dst);
    }

    public Mat warpPerspective(String imagePath) {
        Mat depth = returnImage(imagePath);
        Mat dst = new Mat();
        Imgproc.warpPerspective(depth, dst, getPerspectiveTransform(imagePath), depth.size());
        return dst;
    }


    public void saveImage(String imagePath) {
        Mat image = warpPerspective(imagePath);
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", image);
    }
}
