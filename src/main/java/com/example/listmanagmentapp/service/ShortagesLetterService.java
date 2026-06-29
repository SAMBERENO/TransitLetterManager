package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.dto.RecordsJson;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShortagesLetterService {

    private final RecordsFetchService recordsFetchService;
    private final String inputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/CzysteArkuszeExcel/";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektNaZakladProd/ExeleDoTestow/";
    private final LocalDate date = LocalDate.now();

    public ShortagesLetterService(RecordsFetchService recordsFetchService){
        this.recordsFetchService = recordsFetchService;
    }

    public XSSFWorkbook createShortagesLetter(){
        try(FileInputStream fin = new FileInputStream(inputPath + "FormatkaRuchyBrakow.xlsx")){
            return new XSSFWorkbook(fin);
        } catch (IOException e) {
            System.out.println("RuchyBrakow IO Blad: " + e.getMessage());
        } return null;
    }

    public XSSFWorkbook createShortagesLetterInsiders(){
        try{XSSFWorkbook workbook = createShortagesLetter();
            Sheet sheet = workbook.getSheetAt(0);
            List<RecordsJson> recordsJson = recordsFetchService.fromDBtoDto();
            int rowInData = 0;
            int rowInExcel = 6;
            for(int i = 0; i < 26; i++) {
                if(rowInData < recordsJson.size()) {
                    int excelDamagedCell = 7;
                    rowInExcel += 2;
                    sheet.getRow(rowInExcel).getCell(0).setCellValue(recordsJson.get(rowInData).zmiana());
                    sheet.getRow(rowInExcel).getCell(1).setCellValue(recordsJson.get(rowInData).nrZlecenia());
                    sheet.getRow(rowInExcel).getCell(2).setCellValue(recordsJson.get(rowInData).nrWyrobu());
                    sheet.getRow(rowInExcel).getCell(3).setCellValue(recordsJson.get(rowInData).dataProdukcji());
                    sheet.getRow(rowInExcel).getCell(5).setCellValue(recordsJson.get(rowInData).sumaUszczelek());
                    for(Integer e : recordsJson.get(rowInData).braki().getValues())
                    {
                        if (e != 0) {
                            sheet.getRow(rowInExcel).getCell(excelDamagedCell).setCellValue(e);
                        }
                        excelDamagedCell++;
                    }
                    if (recordsJson.get(rowInData).kz()) {
                        sheet.getRow(rowInExcel).getCell(30).setCellValue("KZ");
                    }
                    rowInData++;
                } else {
                    break;
                }
            } return workbook;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }
    public XSSFWorkbook createShortagesLetterOutsiders(){
        try {XSSFWorkbook workbook = createShortagesLetterInsiders();
            Sheet sheet = workbook.getSheetAt(1);
            List<RecordsJson> recordsJson = recordsFetchService.fromDBtoDto();
            int rowInData = 0;
            int rowInExcel = 6;
            for(int i = 0; i < 26; i++) {
                if(rowInData < recordsJson.size()) {
                    int excelDamagedCell = 5;
                    rowInExcel += 2;
                        String pudla = new StringBuilder().append(recordsJson.get(rowInData).nrZlecenia()).append("$").append(recordsJson.get(rowInData).nrPudla()).toString();
                    sheet.getRow(rowInExcel).getCell(0).setCellValue(pudla);
                    for(Integer e : recordsJson.get(rowInData).braki().getValues())
                    {
                        if (e != 0) {
                            sheet.getRow(rowInExcel).getCell(excelDamagedCell).setCellValue(e);
                        }
                        excelDamagedCell++;
                    }

                    rowInData++;
                } else {
                    break;
                }
            }return workbook;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }

    public void builtLetter(){
        try(XSSFWorkbook workbook = createShortagesLetterOutsiders();
            FileOutputStream fout = new FileOutputStream(outputPath + "Braki " + date + ".xlsx")){
            workbook.setForceFormulaRecalculation(true);
            workbook.write(fout);
        } catch (IOException e) {
            System.out.println("RuchyBrakow IO Blad: " + e.getMessage());
        }
    }

}
