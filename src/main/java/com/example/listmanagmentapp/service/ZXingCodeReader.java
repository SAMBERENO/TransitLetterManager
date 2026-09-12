package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.support.GenericMultipleBarcodeReader;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZXingCodeReader {

    GenericMultipleBarcodeReader genericMultipleBarcodeReader = new GenericMultipleBarcodeReader(new MultiFormatReader());
    private final ImagePreProcessing imagePreProcessing;

    public ZXingCodeReader(ImagePreProcessing imagePreProcessing) {
        this.imagePreProcessing = imagePreProcessing;
    }

    public List<Result[]> decodeImage() throws NotFoundException, IOException {
        List<BufferedImage> bufferedImageList = imagePreProcessing.getBufferedImageList();
        List<Result[]> resultList = new ArrayList<>();
        for (BufferedImage bufferedImage : bufferedImageList) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            resultList.add(genericMultipleBarcodeReader.decodeMultiple(bitmap));
        }
        return resultList;
    }
}