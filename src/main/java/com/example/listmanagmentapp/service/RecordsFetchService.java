package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import com.example.listmanagmentapp.dto.CategoryDamage;
import com.example.listmanagmentapp.dto.JsonFromAndroid;
import com.example.listmanagmentapp.dto.RecordsJson;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordsFetchService {

    public final DBConnectionConfig dbConnectionConfig;
    private final ObjectMapper objectMapper;

    public RecordsFetchService(DBConnectionConfig dbConnectionConfig, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dbConnectionConfig = dbConnectionConfig;
    }

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
}
