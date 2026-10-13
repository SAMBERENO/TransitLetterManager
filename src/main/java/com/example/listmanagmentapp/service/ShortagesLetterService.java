package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.ConfigurationFileReader;
import com.example.listmanagmentapp.config.DbRepository;
import com.example.listmanagmentapp.dto.RecordsJson;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShortagesLetterService {

    private final DbRepository dbRepository;
    private final ConfigurationFileReader configurationFileReader = new ConfigurationFileReader();
    private final String outputPath = configurationFileReader.getLettersOutputPath();
    private final String inputPath = configurationFileReader.getShortagesLetterInputPath();
    private final LocalDate date = LocalDate.now();

    public ShortagesLetterService(DbRepository dbRepository){
        this.dbRepository = dbRepository;
    }

    public XSSFWorkbook createShortagesLetter(){
        try(FileInputStream fin = new FileInputStream(inputPath)){
            return new XSSFWorkbook(fin);
        } catch (IOException e) {
            System.out.println("RuchyBrakow IO Blad: " + e.getMessage());
        } return null;
    }

    public List<XSSFWorkbook> createShortagesLetterInsiders(){
        try {
            List<XSSFWorkbook> excelSheets = new ArrayList<>();
            List<RecordsJson> recordsJson = dbRepository.readZatwierdzone();
            int repeats = recordsJson.size() / 13;
            int rowInData = 0;
            int loop = 0;
            do {
                XSSFWorkbook workbook = createShortagesLetter();
                Sheet sheet = workbook.getSheetAt(0);
                int rowInExcel = 6;
                for (int i = 0; i < 26; i++) {
                    if (rowInData < recordsJson.size()) {
                        int excelDamagedCell = 7;
                        rowInExcel += 2;
                        sheet.getRow(rowInExcel).getCell(0).setCellValue(String.valueOf(recordsJson.get(rowInData).zmiana()));
                        String nrZleceniaiPudla = recordsJson.get(rowInData).nrZleceniaiPudla();
                        String[] nrZlecenia = nrZleceniaiPudla.split("[$]");
                        sheet.getRow(rowInExcel).getCell(1).setCellValue(nrZlecenia[0]);
                        sheet.getRow(rowInExcel).getCell(2).setCellValue(recordsJson.get(rowInData).nrWyrobu());
                        sheet.getRow(rowInExcel).getCell(3).setCellValue(recordsJson.get(rowInData).dataProdukcji());
                        sheet.getRow(rowInExcel).getCell(5).setCellValue(recordsJson.get(rowInData).sumaUszczelek());
                        for (Integer e : recordsJson.get(rowInData).braki().getValues()) {
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
                }
                excelSheets.add(workbook);
                loop++;
            } while (loop <= repeats);
            return excelSheets;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }
    public List<XSSFWorkbook> createShortagesLetterOutsiders(){
        try {
            List<XSSFWorkbook> oldExcelSheets = createShortagesLetterInsiders();
            List<XSSFWorkbook> newExcelSheets = new ArrayList<>();
            for (XSSFWorkbook workbook : oldExcelSheets) {
                Sheet sheet = workbook.getSheetAt(1);
                List<RecordsJson> recordsJson = dbRepository.readZatwierdzone();
                int rowInData = 0;
                int rowInExcel = 6;
                for (int i = 0; i < 26; i++) {
                    if (rowInData < recordsJson.size()) {
                        int excelDamagedCell = 5;
                        rowInExcel += 2;
                        sheet.getRow(rowInExcel).getCell(0).setCellValue(recordsJson.get(rowInData).nrZleceniaiPudla());
                        for (Integer e : recordsJson.get(rowInData).braki().getValues()) {
                            if (e != 0) {
                                sheet.getRow(rowInExcel).getCell(excelDamagedCell).setCellValue(e);
                            }
                            excelDamagedCell++;
                        }

                        rowInData++;
                    } else {
                        break;
                    }
                }
                newExcelSheets.add(workbook);
            } return newExcelSheets;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }

    public void buildLetter(){
        int count = 1;
        List<XSSFWorkbook> workbooks = createShortagesLetterOutsiders();
        for (XSSFWorkbook workbook : workbooks) {
            try (FileOutputStream fout = new FileOutputStream(outputPath + "Braki_" + count + "_" + date + ".xlsx")) {
                workbook.setForceFormulaRecalculation(true);
                workbook.write(fout);
                workbook.close();
                count++;
            } catch (IOException e) {
                System.out.println("RuchyBrakow IO Blad: " + e.getMessage());
            }
        }
    }

}
