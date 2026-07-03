package com.example.listmanagmentapp.service;

import com.example.listmanagmentapp.config.DbRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ListsCreationOrganizerService {

    /* TODO: Po ukończeniu metody createTransitLetter() i createShortagesLetter() zrobić:
        - CodeReview dla tej klasy/klas serwisowych
     */

    private final RecordsFetchService recordsFetchService = new RecordsFetchService(new DbRepository(), new ObjectMapper());
    private final TransitLetterService transitLetterService = new TransitLetterService(recordsFetchService);
    private final ShortagesLetterService shortagesLetterService = new ShortagesLetterService(recordsFetchService);

    public ListsCreationOrganizerService(){}

    public void createLists(){
        transitLetterService.createTransitLetter();
        shortagesLetterService.builtLetter();
    }
}
