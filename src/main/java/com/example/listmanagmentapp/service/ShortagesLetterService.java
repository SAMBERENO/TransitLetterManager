package com.example.listmanagmentapp.service;

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
public class ShortagesLetterService {

    private final RecordsFetchService recordsFetchService;
    private final String inputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/CzysteArkuszeExcel/";
    private final String outputPath = "C:/Users/arek4/OneDrive/Pulpit(1)/ProjektdlaStarego/ExeleDoTestow/";
    private final LocalDate date = LocalDate.now();

    public ShortagesLetterService(RecordsFetchService recordsFetchService){
        this.recordsFetchService = recordsFetchService;
    }

    public void createShortagesLetter(){
        try(FileInputStream fin = new FileInputStream(inputPath + "FormatkaRuchyBrakow.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fin);
            FileOutputStream fout = new FileOutputStream(outputPath + "Braki " + date + ".xlsx")){

            Sheet sheet = workbook.getSheetAt(0);

            List<RecordsJson> recordsJson = recordsFetchService.fromDBtoDto();
            int rowInData = 0;
            int rowInExcel = 6;
            String pudla = "";
            String[] powtorki = new String[3];
            for(int i = 0; i < 26; i++) {
                if(rowInData < recordsJson.size()) {
                    int celloInExcello = 7;

                    rowInExcel+=2;
                    if (recordsJson.get(rowInData).nrWyrobu().matches(".*//.*")) {
                        sheet.getRow(rowInExcel).getCell(1).setCellValue(powtorki[0]);
                        sheet.getRow(rowInExcel).getCell(2).setCellValue(powtorki[1]);
                        sheet.getRow(rowInExcel).getCell(3).setCellValue(powtorki[2]);
                    } else {
                        sheet.getRow(rowInExcel).getCell(1).setCellValue(recordsJson.get(rowInData).nrZlecenia());
                        sheet.getRow(rowInExcel).getCell(2).setCellValue(recordsJson.get(rowInData).nrWyrobu());
                        sheet.getRow(rowInExcel).getCell(3).setCellValue(recordsJson.get(rowInData).dataOdbioru());
                        powtorki[0] = recordsJson.get(rowInData).nrZlecenia();
                        powtorki[1] = recordsJson.get(rowInData).nrWyrobu();
                        powtorki[2] = recordsJson.get(rowInData).dataOdbioru();
                    }

                    pudla = String.valueOf(recordsJson.get(rowInData).pudla().charAt(0));
                    sheet.getRow(rowInExcel).getCell(0).setCellValue(pudla);
                    sheet.getRow(rowInExcel).getCell(5).setCellValue(recordsJson.get(rowInData).sumaUszczelek());

                    for(Integer e : recordsJson.get(rowInData).braki().getValues())
                    {
                        if (e != 0) {
                            sheet.getRow(rowInExcel).getCell(celloInExcello).setCellValue(e);
                        }
                        celloInExcello++;
                    }
                    if (recordsJson.get(rowInData).kz()) {
                        sheet.getRow(rowInExcel).getCell(30).setCellValue("KZ");
                    }
                    rowInData++;

                } else {
                    break;
                }
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
    }}
