package com.example.listmanagmentapp.config;

import com.example.listmanagmentapp.dto.RecordsJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DbRepository {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final JdbcTemplate jdbcTemplate;

    public DbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Integer, String> readAll() {
        String query = "SELECT * FROM DaneJson";
        return jdbcTemplate.query(query, e -> {
            Map<Integer, String> map = new HashMap<>();
            while (e.next()) {
                map.put(e.getInt(0), e.getString(1));
            }
            return map;
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

    public void addJson(String json) {
        String query = "INSERT INTO DaneJson (json) VALUES (?)";
        objectMapper.readValue(json, RecordsJson.class);
        jdbcTemplate.update(query, json);
    }
}
