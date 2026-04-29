package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.Statement;

@Service
public class ListCreatingService {

    private final DBConnectionConfig dbConnectionConfig;

    public ListCreatingService(DBConnectionConfig dbConnectionConfig) {
        this.dbConnectionConfig = dbConnectionConfig;
    }

    private String inputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/CzysteArkuszeExcel/";
    private String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/ExeleDoTestow/";

    public void createTranzitLetter(){
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement();
            FileInputStream fin = new FileInputStream(inputPath + "FormatkaListu.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "List.xlsx")){
            workbook.write(fout);

        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku ListPrzewozowy: " + e.getMessage());
        } catch (FileAlreadyExistsException e) {
            System.out.println("Arkusz ListPrzewozowy istnieje: " + e.getMessage());
        } catch (POIXMLException e) {
            System.out.println("Plik ListPrzewozowy Excel jest uszkodzony lub pusty: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ListPrzewozowy IO Blad: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void createShortagesLetter(){
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement();
            FileInputStream fin = new FileInputStream(inputPath + "FormatkaRuchyBrakow.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "Braki.xlsx")){
            workbook.write(fout);


        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku RuchyBrakow: " + e.getMessage());
        } catch (FileAlreadyExistsException e) {
            System.out.println("Arkusz RuchyBrakow istnieje: " + e.getMessage());
        } catch (POIXMLException e) {
            System.out.println("Plik RuchyBrakow Excel jest uszkodzony lub pusty: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("RuchyBrakow IO Blad: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
