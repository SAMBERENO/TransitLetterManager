package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.ConfigurationFileReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class PDFHandler {

    public PDFHandler() {}

    public String returnPDF() throws IOException {
        Path path = Path.of(ConfigurationFileReader.getPDFsFolderPath());
        Path file = Files.list(path)
                .filter(e -> e.toString().endsWith(".pdf"))
                .max(Comparator.comparingLong(e -> e.toFile().lastModified()))
                .orElseThrow();
        return file.toString();
    }

    public void removeUsedPDF() throws IOException {
        Path path = Path.of(returnPDF());
        Files.delete(path);
    }
}
