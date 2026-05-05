package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.dto.CategoryDamage;
import com.example.listmanagmentapp.dto.CodesWithValues;
import com.example.listmanagmentapp.dto.JsonFromAndroid;
import com.example.listmanagmentapp.dto.RecordsJson;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListCreatingService {

    private final DBConnectionConfig dbConnectionConfig;
    private final ObjectMapper objectMapper;

    public ListCreatingService(DBConnectionConfig dbConnectionConfig, ObjectMapper objectMapper) {
        this.dbConnectionConfig = dbConnectionConfig;
        this.objectMapper = objectMapper;
    }

    private final String inputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/CzysteArkuszeExcel/";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/ExeleDoTestow/";
    private final LocalDate date = LocalDate.now();

    public List<RecordsJson> fromDBtoDto() {
        List<RecordsJson> recordsJson = new ArrayList<>();
        try (Connection connection = dbConnectionConfig.dbConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT json FROM DaneJson")) {
            List<JsonFromAndroid> jsonFromAndroid = new ArrayList<>();
            while (rs.next()) {
                jsonFromAndroid.add(objectMapper.readValue(rs.getString("json"), JsonFromAndroid.class));
            }
            for (JsonFromAndroid jsonIndex : jsonFromAndroid) {
                for (RecordsJson recordsIndex : jsonIndex.rekordy()) {

                    recordsIndex = new RecordsJson(recordsIndex.pudla(), recordsIndex.nrWyrobu(), recordsIndex.nrZlecenia(), recordsIndex.dataOdbioru(), recordsIndex.sumaUszczelek(), recordsIndex.sumaBrakow(), recordsIndex.niezgodnosci(), recordsIndex.kz(),
                            new CategoryDamage(recordsIndex.braki().A(), recordsIndex.braki().B(), recordsIndex.braki().C(), recordsIndex.braki().D(), recordsIndex.braki().E(), recordsIndex.braki().F(), recordsIndex.braki().G(), recordsIndex.braki().H(), recordsIndex.braki().I(), recordsIndex.braki().J(), recordsIndex.braki().K(), recordsIndex.braki().L(), recordsIndex.braki().M(), recordsIndex.braki().N(), recordsIndex.braki().O(), recordsIndex.braki().P(), recordsIndex.braki().R(), recordsIndex.braki().S(), recordsIndex.braki().T(), recordsIndex.braki().U(), recordsIndex.braki().V(), recordsIndex.braki().W(), recordsIndex.braki().X()));

                    recordsJson.add(recordsIndex);
                }
            }
        } catch (SQLException e){System.out.println("SQLException event: " + e.getMessage());}
        return recordsJson;
    }


    public void createTransitLetter(){
        try(FileInputStream fin = new FileInputStream(inputPath + "FormatkaListu.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "List " + date + ".xlsx")){

            Sheet sheet = workbook.getSheetAt(0);

            int row = 0;
            for(int i = 17; i < 30; i++) {
                if(row >= fromDBtoDto().size()){
                    break;
                }
                sheet.getRow(i).getCell(1).setCellValue(fromDBtoDto().get(row).nrWyrobu());
                sheet.getRow(i).getCell(2).setCellValue(fromDBtoDto().get(row).nrZlecenia());
                sheet.getRow(i).getCell(3).setCellValue(fromDBtoDto().get(row).sumaUszczelek());
                sheet.getRow(i).getCell(4).setCellValue(fromDBtoDto().get(row).sumaBrakow());
                for(CodesWithValues e : fromDBtoDto().get(row).braki().getMap().values()) {
                    if (e.value() != 0) {
                        String damagedResults = new StringBuilder().append(e.value()).append(e.letter()).toString();
                        sheet.getRow(i).getCell(7).setCellValue(damagedResults);
                    }
                }
                sheet.getRow(i).getCell(8).setCellValue(fromDBtoDto().get(row).niezgodnosci());
                if (fromDBtoDto().get(row).kz()){
                    sheet.getRow(i).getCell(9).setCellValue("KZ");
                }
                row++;
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
        try(FileInputStream fin = new FileInputStream(inputPath + "FormatkaRuchyBrakow.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "Braki " + date + ".xlsx")){




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
