package com.example.listmanagmentapp.config;

import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class ConfigurationFileReader {

    private static final Properties properties = new  Properties();

    public ConfigurationFileReader() {
        try (FileInputStream fis = new FileInputStream("configuration.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Nie znaleziono pliku configuration.properties " + e.getMessage());
        }
    }

    public String getExcelFilesOutputPath() {
        return properties.getProperty("ExcelFilesOutputPath");
    }

    public String getExcelFilesInputPath() {
        return properties.getProperty("ExcelFilesInputPath");
    }

    public static String getPDFsFolderPath() { return properties.getProperty("FolderWithPDFs"); }

}
