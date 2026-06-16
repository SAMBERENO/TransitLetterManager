package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.dto.CenterOfRect;
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

    private Point center(Rect rect) {
        int centerX = rect.x + (rect.width / 2);
        int centerY = rect.y + (rect.height / 2);
        return new Point(centerX, centerY);
    }

    private boolean isInRange(Point point0, Point point1){
        return (point0.x + 40 >= point1.x || point0.x - 40 <= point1.x) && point0.y + 20 >= point1.y && point0.y - 20 <= point1.y;
    }

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
        Imgproc.findContours(source, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
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
        List<CenterOfRect> rectangles = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            CenterOfRect rectCenter = new CenterOfRect(rect ,center(rect));
            rectangles.add(rectCenter);
            //if (rect.width < 500 && rect.height < 150 && rect.width > 20 && rect.height > 10) Imgproc.rectangle(morphologyImage, rect.tl(), rect.br(), new Scalar(255, 0, 0), -1);
        }

        Mat mat = Mat.zeros(morphologyImage.rows(), morphologyImage.cols(), CvType.CV_8UC1);

        for (CenterOfRect rectangle : rectangles) {
            mat.put((int) rectangle.center().x, (int) rectangle.center().y, 255);
        }

        List<List<Point>> spansList = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                double value = mat.get(j, i)[0];
                double valueLeft = 0;
                double valueRight = 0;
                if (value != 0) {
                    points.add(new Point(j, i)); //Zapisuje punkt startowy grupy do listy
                    mat.put(j, i, 0);
                    do {
                        //Rect leftCheck = new Rect(j - 15, i + 5, 14,10);
                        Rect rightCheck = new Rect(j + 1, i + 5, 15, 10);
                        Mat checkLeft = new Mat(morphologyImage, new Rect(j - 15, i + 5, 14, 10));
                        for (int rows = 0; rows < checkLeft.rows(); rows++) {
                            for (int cols = 0; cols < checkLeft.cols(); cols++) {
                                valueLeft = checkLeft.get(cols, rows)[0];
                                if (valueLeft != 0) {
                                    points.add(new Point(cols, rows));
                                    mat.put(cols, rows, 0);
                                    break; //Sprawdzić zakres, tzn. czy wywali mnie z jednego loopa czy ze wszystkich xD
                                }
                            }
                        }

                    } while (valueLeft == 0); //Zmienić value na wartość przypisywaną w tym do while
                }
            }
        }

        Imgcodecs.imwrite("morphed.jpg", morphologyImage);
        return morphologyImage;
    }

    /* TODO:
    - Utworzenie Mat() z punktami w środku każdego prostokątu
    - Pętla skacząca po każdej kolumnie i rzędzie w poszukiwaniu tych punktów
    - Znaleziony punkt zostaje dodany do nowej kolekcji, a następnie wymazany z Mat()
    - Od współrzędnych wymazanego punktu utworzyć prostokąt, który będzie polem do przeszukania kolejnych sąsiednich punktów
    - Powtórzyć od 2 kroku dla mniejszego utworzonego prostokąta
    - Jeśli pętla niczego już nie znajdzie w utworzonych prostokątach, należy wrócić do kroku 2 na pełnym Mat()
     */


    public void saveImage(String imagePath) {
        Mat image = morphologyDilation(imagePath);
        Imgcodecs.imwrite("C:\\Users\\arek4\\OneDrive\\Pulpit(1)\\ProjektNaZakladProd\\ZdjeciaDoSkanowania\\1.jpg", image);
    }

    /* TODO:
    Do zrobienia spanów:
1. --findContours / connectedComponents
2. --boundingRect dla każdego elementu
3. --policz odległości między elementami
4. połącz bliskie elementy
5. utwórz grupę (span)
6. utwórz jeden duży bounding box dla grupy
     */

}
