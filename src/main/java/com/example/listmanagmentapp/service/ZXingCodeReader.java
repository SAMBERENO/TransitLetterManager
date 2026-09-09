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
import java.util.EnumMap;
import java.util.Map;

@Service
public class ZXingCodeReader {

    GenericMultipleBarcodeReader genericMultipleBarcodeReader = new GenericMultipleBarcodeReader(new MultiFormatReader());

    Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);




    public Result[] decodeImage(String pathName) throws NotFoundException, IOException {
        PDDocument document = Loader.loadPDF(new File(pathName));
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 600);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        return genericMultipleBarcodeReader.decodeMultiple(bitmap);
    }

}