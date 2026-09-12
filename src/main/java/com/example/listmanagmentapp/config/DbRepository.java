package com.example.listmanagmentapp.config;

import com.example.listmanagmentapp.dto.CategoryDamage;
import com.example.listmanagmentapp.dto.JsonFromAndroid;
import com.example.listmanagmentapp.dto.QrRecords;
import com.example.listmanagmentapp.dto.RecordsJson;
import com.example.listmanagmentapp.service.PDFHandler;
import com.example.listmanagmentapp.service.ZXingCodeReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DbRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JdbcTemplate jdbcTemplate;
    private final ZXingCodeReader zxingCodeReader;
    private final PDFHandler pdfHandler;

    public DbRepository(JdbcTemplate jdbcTemplate,  ZXingCodeReader zxingCodeReader, PDFHandler pdfHandler) {
        this.jdbcTemplate = jdbcTemplate;
        this.zxingCodeReader = zxingCodeReader;
        this.pdfHandler = pdfHandler;
    }

    public String getJsonByID(String nrZleceniaiPudla) {
        String query = "SELECT * FROM DaneJson WHERE nrZlecenia = ?";
        return jdbcTemplate.query(query, e -> {
            if (e.next()) {
                return e.getString("json");
            }
            return null;
        }, nrZleceniaiPudla);
    }

    public List<RecordsJson> readZatwierdzone() {
        String query = "SELECT json FROM DaneJson WHERE zatwierdzone = 1";
        return jdbcTemplate.query(query, e -> {
            List<RecordsJson> recordsJson= new ArrayList<>();
            while (e.next()) {
                recordsJson.add(objectMapper.readValue(e.getString("json"), RecordsJson.class));
            }
            return recordsJson;
        });
    }

    public List<RecordsJson> readJson() {
        String query = "SELECT json FROM DaneJson";
        return jdbcTemplate.query(query, e -> {
            List<RecordsJson> recordsJson= new ArrayList<>();
            while (e.next()) {
                recordsJson.add(objectMapper.readValue(e.getString("json"), RecordsJson.class));
            }
            return recordsJson;
        });
    }

    public int dbRecordsCount() {
        String query = "SELECT count(*) FROM DaneJson WHERE zatwierdzone = 1";
        return jdbcTemplate.query(query, e -> {
            e.next();
            return e.getInt(1);
        });
    }

    public void addJsonFromAndroid(String json){
        String query = "INSERT INTO DaneJson (nrZlecenia, json) VALUES (?, ?)";
        JsonFromAndroid jsonFromAndroid = objectMapper.readValue(json, JsonFromAndroid.class);
        for (QrRecords qrRecords : jsonFromAndroid.rekordy()) {
            RecordsJson recordsJson = new RecordsJson(qrRecords.zmiana(), qrRecords.nrWyrobu(), qrRecords.nrZleceniaiPudla(), qrRecords.dataProdukcji(), qrRecords.sumaUszczelek(), 0, 0, false,
                    new CategoryDamage(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            String jsonString = objectMapper.writeValueAsString(recordsJson);
            jdbcTemplate.update(query, qrRecords.nrZleceniaiPudla(), jsonString);
        }
    }

    public void addRecordsFromPDF() throws NotFoundException, IOException {
        String query = "INSERT INTO DaneJson (nrZlecenia, json) VALUES (?, ?)";
        for  (Result[] results : zxingCodeReader.decodeImage()) {
            RecordsJson recordsJson = new RecordsJson('X', results[1].toString(), results[0].toString(), null, Integer.parseInt(results[2].toString()), 0, 0, false,
                    new CategoryDamage(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            String jsonString = objectMapper.writeValueAsString(recordsJson);
            jdbcTemplate.update(query, results[0].toString(), jsonString);
        }
    }

    public void addJson(String json) {
        String query = "INSERT INTO DaneJson (nrZlecenia, json) VALUES (?, ?)";
        RecordsJson nrZleceniaiPudla = objectMapper.readValue(json, RecordsJson.class);
        jdbcTemplate.update(query, nrZleceniaiPudla.nrZleceniaiPudla(), json);
    }

    public void updateRecord(String json) {
        String query = "UPDATE DaneJson SET json = ?, zatwierdzone = 1 WHERE nrZlecenia = ?";
        RecordsJson nrZlecenia = objectMapper.readValue(json, RecordsJson.class);
        jdbcTemplate.update(query, json, nrZlecenia.nrZleceniaiPudla());
    }

    public void deleteAll() {
        String query = "DELETE FROM DaneJson";
        jdbcTemplate.update(query);
    }

    public void deleteOne(String id) {
        String query = "DELETE FROM DaneJson WHERE id=?";
        jdbcTemplate.update(query, id);
    }

    public void deleteZatwierdzone() {
        String query = "DELETE FROM DaneJson WHERE zatwierdzone = 1";
        jdbcTemplate.update(query);
    }
}
