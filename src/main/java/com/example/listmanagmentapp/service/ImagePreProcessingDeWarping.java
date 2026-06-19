package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.model.CenterOfRect;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ImagePreProcessingDeWarping {

    public ImagePreProcessingDeWarping() {}

    private Mat returnImage(String imagePat){return Imgcodecs.imread(imagePat);}

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

    public List<MatOfPoint> findContours(Mat inputMat) {
        Mat source = inputMat.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(source, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public Mat verticalLinesRemoval(Mat inputMat){
        Mat medianImage = inputMat.clone();
        Mat verticalKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 40));
        Mat verticalMorphology = new Mat();
        Mat cleanedImage = new Mat();

        Imgproc.morphologyEx(medianImage, verticalMorphology, Imgproc.MORPH_CLOSE, verticalKernel);
        Imgproc.morphologyEx(verticalMorphology, verticalMorphology, Imgproc.MORPH_DILATE, verticalKernel);

        Photo.inpaint(medianImage, verticalMorphology, cleanedImage, 1, Photo.INPAINT_TELEA);
        return cleanedImage;
    }

    public Mat morphologyDilation(String imagePath){
        Mat medianImage = medianBlur(imagePath);
        List<MatOfPoint> contours = findContours(medianImage);
        List<CenterOfRect> rectangles = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            CenterOfRect rectCenter = new CenterOfRect(rect);
            rectangles.add(rectCenter);
        }

        Mat mat = Mat.zeros(medianImage.rows(), medianImage.cols(), CvType.CV_8UC1);

        for (CenterOfRect rectangle : rectangles) {
            mat.put((int) rectangle.center().y, (int) rectangle.center().x, 255);
        }

        int rectWidth = 60;
        int rectHeight = 10;

        List<List<Point>> spansList = new ArrayList<>();
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                boolean nextLoop = false;
                double value = mat.get(i, j)[0];
                if (value != 0) {
                    List<Point> points = new ArrayList<>();
                    points.add(new Point(j, i));
                    mat.put(i, j, 0);
                    do {
                        int getLastX = (int) points.getLast().x;
                        int getLastY = (int) points.getLast().y;
                        int rectX = Math.min(getLastX, rectWidth);
                        int rectPointY = Math.min(getLastY, rectHeight);
                        int rectHeightY = Math.min(rectHeight * 2, mat.rows() - (getLastY - rectHeight));
                        Mat checkLeft = new Mat(mat, new Rect(getLastX - rectX, getLastY - rectPointY, rectX, rectHeightY));
                        checkLeft:
                        for (int rows = checkLeft.rows() - 1; rows > 0; rows--) {
                            for (int cols = checkLeft.cols() - 1; cols > 0; cols--) {
                                double valueLeft = checkLeft.get(rows, cols)[0];
                                if (valueLeft != 0) {
                                    points.add(new Point(getLastX - rectX + cols, getLastY - rectPointY + rows));
                                    mat.put((int) points.getLast().y, (int) points.getLast().x, 0);
                                    nextLoop = true;
                                    break checkLeft;
                                }
                                nextLoop = false;
                            }
                        }
                    } while (nextLoop);
                    //points.add i points.removeFirst są po to by wrócić ponownie do punktu startowego i przejść w prawą stronę
                    points.add(points.getFirst());
                    points.removeFirst();
                    do {
                        int getLastX = (int) points.getLast().x;
                        int getLastY = (int) points.getLast().y;
                        int rectX = Math.min(mat.cols() - getLastX, rectWidth);
                        int rectPointY = Math.min(getLastY, rectHeight);
                        int rectHeightY = Math.min(rectHeight * 2, mat.rows() - (getLastY - rectHeight));
                        Mat checkRight = new Mat(mat, new Rect(getLastX, getLastY - rectPointY, rectX, rectHeightY));
                        checkRight:
                        for (int rows = 0; rows < checkRight.rows(); rows++) {
                            for (int cols = 0; cols < checkRight.cols(); cols++) {
                                double valueRight = checkRight.get(rows, cols)[0];
                                if(valueRight != 0) {
                                    points.add(new Point(getLastX + cols, getLastY - rectPointY + rows));
                                    mat.put((int) points.getLast().y,(int) points.getLast().x, 0);
                                    nextLoop = true;
                                    break checkRight;
                                }
                                nextLoop = false;
                            }
                        }
                    } while (nextLoop);
                    if (points.size() > 1)
                        spansList.add(points);
                }
            }
        }
        for (List<Point> points : spansList) {
            points.sort(Comparator.comparingDouble(point -> point.x));
            //Do debugowania
            System.out.println("Nowa grupa:");
            for (Point point : points) {
                Imgproc.circle(mat, point, 3, new Scalar(255, 0, 0), -1);
                System.out.println(point);
            }
        }
        return mat;
    }

    public void saveImage(String imagePath) {
        Mat image = morphologyDilation(imagePath);
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", image);
    }

    /* TODO:
    Do zrobienia spanów:
1. --findContours / connectedComponents
2. --boundingRect dla każdego elementu
3. --policz odległości między elementami
4. --połącz bliskie elementy
5. --utwórz grupę (span)
6. utwórz grupy do grupowania po wysokości/linii
     */

}
