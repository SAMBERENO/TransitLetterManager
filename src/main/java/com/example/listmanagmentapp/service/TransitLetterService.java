package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DbRepository;
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
import java.time.LocalDate;
import java.util.List;

@Service
public class TransitLetterService {

    private final DbRepository dbRepository;
    private final String inputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/CzysteArkuszeExcel/";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ExeleDoTestow/";
    private final LocalDate date = LocalDate.now();

    public TransitLetterService(DbRepository dbRepository){
        this.dbRepository = dbRepository;
    }

    public void createTransitLetter(){
        try(FileInputStream fin = new FileInputStream(inputPath + "FormatkaListu.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "List " + date + ".xlsx")){

            Sheet sheet = workbook.getSheetAt(0);

            List<RecordsJson> recordsJson = dbRepository.readJson();
            int rowInData = 0;
            int rowInExcel = 16;
            int sumaDobrychUszczelek = 0;
            int sumaBrakow = 0;
            int sumaNiezgodnosci = 0;
            String pudla = "";
            for(int i = 0; i < 12; i++) {
                if (rowInData < recordsJson.size()) {
                if (rowInData != 0 && recordsJson.get(rowInData).nrWyrobu().matches(recordsJson.get(rowInData - 1).nrWyrobu())) {
                    pudla = "," + pudla;
                } else {
                    rowInExcel++;
                    pudla = "";
                    sumaDobrychUszczelek = 0;
                    sumaBrakow = 0;
                    sumaNiezgodnosci = 0;
                    sheet.getRow(rowInExcel).getCell(1).setCellValue(recordsJson.get(rowInData).nrWyrobu());
                    sheet.getRow(rowInExcel).getCell(2).setCellValue(recordsJson.get(rowInData).nrZlecenia());
                }
                //TODO: Możliwe że będzie trzeba zmniejszyć zakres w StringBuilder().delete(0, 3) na .delete(0, 2)
                //TODO: Rozkminić jak program ma wiedzieć czy numer pudła ma 1 czy 2 cyfry
                pudla = new StringBuilder().append(recordsJson.get(rowInData).nrPudla()).append(pudla).toString();
                sheet.getRow(rowInExcel).getCell(3).setCellValue(sumaDobrychUszczelek += recordsJson.get(rowInData).sumaUszczelek() - recordsJson.get(rowInData).sumaBrakow());
                sheet.getRow(rowInExcel).getCell(4).setCellValue(sumaBrakow += recordsJson.get(rowInData).sumaBrakow());
                sheet.getRow(rowInExcel).getCell(8).setCellValue(sumaNiezgodnosci += recordsJson.get(rowInData).niezgodnosci());
                sheet.getRow(rowInExcel).getCell(7).setCellValue(pudla);
                if (recordsJson.get(rowInData).kz()) {
                    sheet.getRow(rowInExcel).getCell(9).setCellValue("KZ");
                }
                rowInData++;
            } else {
                    break;
                }
            }
            workbook.setForceFormulaRecalculation(true);
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
    }}
