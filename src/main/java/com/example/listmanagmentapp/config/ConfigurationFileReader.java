package com.example.listmanagmentapp.config;

import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class ConfigurationFileReader {

    private final Properties properties = new  Properties();

    public ConfigurationFileReader() {
        try (FileInputStream fis = new FileInputStream("releaseApp/ListManagementApp/configuration.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getShortagesLetterPath(){
        return properties.getProperty("ShortagesLetterOutputPath");
    }

    public String getTransitLetterPath(){
        return properties.getProperty("TransitLetterOutputPath");
    }

}
