package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.dto.CategoryDamaged;
import com.example.listmanagmentapp.dto.JsonFromAndroid;
import com.example.listmanagmentapp.dto.RecordsJson;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

@Service
public class ListCreatingService {

    private final DBConnectionConfig dbConnectionConfig;

    public ListCreatingService(DBConnectionConfig dbConnectionConfig) {
        this.dbConnectionConfig = dbConnectionConfig;
    }

    private final String inputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/CzysteArkuszeExcel/";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/ExeleDoTestow/";
    private final LocalDate date = LocalDate.now();
    private CategoryDamaged categoryDamaged;
    private RecordsJson recordsJson;
    private JsonFromAndroid jsonFromAndroid;

    public void createTranzitLetter(){
        try(Connection connection = dbConnectionConfig.dbConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT json FROM DaneJson");
            FileInputStream fin = new FileInputStream(inputPath + "FormatkaListu.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "List " + date + ".xlsx")){
            while (rs.next()){

                

            }


            Sheet sheet = workbook.getSheetAt(0);
            for(int i = 17; i < 25; i++) {
                sheet.getRow(i).getCell(1).setCellValue(date.toString());
            }

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
            ResultSet rs = statement.executeQuery("SELECT json FROM DaneJson");
            FileInputStream fin = new FileInputStream(inputPath + "FormatkaRuchyBrakow.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "Braki " + date + ".xlsx")){
            while (rs.next()){

            }



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
