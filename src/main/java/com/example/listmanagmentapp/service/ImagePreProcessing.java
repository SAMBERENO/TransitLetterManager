package com.example.listmanagmentapp.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImagePreProcessing {

    private final PDFHandler pdfHandler;

    public ImagePreProcessing(PDFHandler pdfHandler) {
        this.pdfHandler = pdfHandler;
    }

    private Mat returnedImage;

    //Zmienić sposób przekazania zdjęcia z .pdf na .jpg
    private void returnImage() {
        try (PDDocument document = Loader.loadPDF(new File(pdfHandler.returnPDF()))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 500, ImageType.BINARY);
            ImageIO.write(image, "jpg", new File("jpgPhoto.jpg"));
            returnedImage = Imgcodecs.imread("jpgPhoto.jpg", Imgcodecs.IMREAD_GRAYSCALE);
            Imgcodecs.imwrite("jpgPhotoInv.jpg", returnedImage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<MatOfPoint> findContours() {
        Mat image = new Mat();
        Core.bitwise_not(returnedImage, image);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public List<Mat> BarCodesRemoval() throws IOException {
        List<MatOfPoint> contours = findContours();
        List<Rect> rects = new ArrayList<>();
        List<Mat> barCodeRows = new ArrayList<>();
        for (MatOfPoint matOfPoint : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(matOfPoint.toArray());
            double perimeter = Imgproc.arcLength(contour2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f, approx, 0.02 * perimeter, true);
            if (approx.total() == 4) {
                Rect rect = Imgproc.boundingRect(matOfPoint);
                rects.add(rect);
            }
        }
        for (int i = 0; i < rects.size(); i++) {
            if (rects.get(i).height <= 150 && rects.get(i).height >= 100 && rects.get(i).width <= 30) {
                Mat cutOutMat = returnedImage.submat(rects.get(i).y, rects.get(i).y + rects.get(i).height, 0, returnedImage.cols());
                barCodeRows.add(cutOutMat);
                returnedImage.submat(Imgproc.boundingRect(cutOutMat)).setTo(Scalar.all(0));
                for (int y = 0; y < rects.size(); y++) {
                    if (rects.get(y).y >= rects.get(i).y && rects.get(y).y <= rects.get(i).y + rects.get(i).height) {
                        rects.remove(y);
                    }
                }}}return barCodeRows;
    }

    public List<BufferedImage> getBufferedImageList() throws IOException {
        returnImage();
        List<Mat> matList = BarCodesRemoval();
        List<BufferedImage> bufferedImageList = new ArrayList<>();
        for (Mat mat : matList) {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(byteArray));
            bufferedImageList.add(bufferedImage);
        }
        pdfHandler.removeUsedPDF();
        return bufferedImageList;
    }
}