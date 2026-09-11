package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.support.GenericMultipleBarcodeReader;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.ByQuadrantReader;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ZXingCodeReader {

    GenericMultipleBarcodeReader genericMultipleBarcodeReader = new GenericMultipleBarcodeReader(new MultiFormatReader());
    ImagePreProcessingDeWarping imagePreProcessingDeWarping = new ImagePreProcessingDeWarping();



    public List<Result[]> decodeImage(String pathName) throws NotFoundException, IOException {
        List<BufferedImage> bufferedImageList = imagePreProcessingDeWarping.getBufferedImageList(pathName);
        List<Result[]> resultList = new ArrayList<>();
        for (BufferedImage bufferedImage : bufferedImageList) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            resultList.add(genericMultipleBarcodeReader.decodeMultiple(bitmap));
        }
        return resultList;
    }
}