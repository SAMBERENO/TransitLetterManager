package com.example.listmanagmentapp.service;

import org.springframework.stereotype.Service;

@Service
public class ListsCreationOrganizerService {

    /* TODO: Po ukończeniu metody createTransitLetter() i createShortagesLetter() zrobić:
        - CodeReview dla tej klasy/klas serwisowych
     */

    private final TransitLetterService transitLetterService;
    private final ShortagesLetterService shortagesLetterService;

    public ListsCreationOrganizerService(TransitLetterService transitLetterService, ShortagesLetterService shortagesLetterService){
        this.transitLetterService = transitLetterService;
        this.shortagesLetterService = shortagesLetterService;
    }

    public void createLists(){
        transitLetterService.createTransitLetter();
        shortagesLetterService.buildLetter();
    }
}
