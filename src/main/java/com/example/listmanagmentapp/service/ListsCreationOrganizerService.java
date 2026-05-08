package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ListsCreationOrganizerService {

    /* TODO: Po ukończeniu metody createTransitLetter() i createShortagesLetter() zrobić:
        - CodeReview dla tej klasy/klas serwisowych
     */

    private final RecordsFetchService recordsFetchService;
    private final TransitLetterService transitLetterService;
    private final ShortagesLetterService shortagesLetterService;

    public ListsCreationOrganizerService(DBConnectionConfig dbConnectionConfig, ObjectMapper objectMapper){
        this.recordsFetchService = new RecordsFetchService(dbConnectionConfig, objectMapper);
        this.transitLetterService = new TransitLetterService(recordsFetchService);
        this.shortagesLetterService = new ShortagesLetterService(recordsFetchService);
    }

    public void createLists(){
        transitLetterService.createTransitLetter();
        shortagesLetterService.builtLetter();
    }
}
