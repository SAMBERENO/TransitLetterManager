package com.example.listmanagmentapp;

import com.example.listmanagmentapp.config.DbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ListManagementAppTests {

    @Autowired
    private DbRepository dbRepository;

    @Test
    void contextLoads() {
        assertEquals(4, dbRepository.dbRecordsCount());
    }

    @Test
    void getAllZatwierdzoneRecords() {
        assertEquals(1, dbRepository.readZatwierdzone().size());
    }
}
