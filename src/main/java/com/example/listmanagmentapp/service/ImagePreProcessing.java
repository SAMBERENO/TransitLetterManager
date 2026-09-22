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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Mat> BarCodesRemoval() {
        try {
            List<Mat> barCodeList = new ArrayList<>();
            int rowStart = 627;
            int rowEnd = 846;
            for (int i = 0; i <= 21; i++) {
                boolean foundPrint = false;
                Mat barCodesRow = returnedImage.submat(rowStart, rowEnd, 0, returnedImage.cols());
                for (int y = 0; y < barCodesRow.rows(); y++) {
                    for (int x = 0; x < barCodesRow.cols(); x++) {
                        double[] pixel = barCodesRow.get(y, x);
                        if (pixel[0] == 0) {
                            barCodeList.add(barCodesRow);
                            rowStart += 219;
                            rowEnd += 219;
                            foundPrint = true;
                            break;
                        }
                    }
                    if (foundPrint)
                        break;
                }
                if (!foundPrint)
                    break;
            }
            return barCodeList;
        } catch (Exception e) {
            System.out.println("BarCodesRemoval() exception: " + e.getMessage());
            return null;
        }
    }

    public List<BufferedImage> getBufferedImageList() throws IOException {
        try {
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
        } catch (Exception e) {
            System.out.println("getBufferedImageList() exception: " + e.getMessage());
            return null;
        }
    }
}