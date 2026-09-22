package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.support.GenericMultipleBarcodeReader;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Service
public class ZXingCodeReader {

    GenericMultipleBarcodeReader genericMultipleBarcodeReader = new GenericMultipleBarcodeReader(new MultiFormatReader());
    private final ImagePreProcessing imagePreProcessing;

    public ZXingCodeReader(ImagePreProcessing imagePreProcessing) {
        this.imagePreProcessing = imagePreProcessing;
    }

    public List<Result[]> decodeImage() throws IOException {
        try {
            List<BufferedImage> bufferedImageList = imagePreProcessing.getBufferedImageList();
            List<Result[]> resultList = new ArrayList<>();
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(
                    DecodeHintType.POSSIBLE_FORMATS,
                    List.of(BarcodeFormat.CODE_39)
            );
            for (BufferedImage bufferedImage : bufferedImageList) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
                resultList.add(genericMultipleBarcodeReader.decodeMultiple(bitmap, hints));
            }
            return resultList;
        } catch (NotFoundException e) {
            System.out.println("decodeImage() exception: " + e.getMessage());
            return null;
        }
    }
}