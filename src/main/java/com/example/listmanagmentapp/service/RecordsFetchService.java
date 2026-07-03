package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DbRepository;
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

    public final DbRepository dbRepository;
    private final ObjectMapper objectMapper;

    public RecordsFetchService(DbRepository dbRepository, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dbRepository = dbRepository;
    }

    public List<RecordsJson> fromDBtoDto() {
        List<RecordsJson> recordsJson = new ArrayList<>();
        try (Connection connection = dbRepository.dbConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT json FROM DaneJson")) {
            while (rs.next()) {
                recordsJson.add(objectMapper.readValue(rs.getString("json"), RecordsJson.class));
            }
        } catch (SQLException e){System.out.println("SQLException event: " + e.getMessage());}
        return recordsJson;
    }
}
