package com.example.listmanagmentapp.service;

import org.springframework.stereotype.Service;

@Service
public class ListsCreationOrganizerService {

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

    public void createShortagesList(){
        transitLetterService.createTransitLetter();
    }
}
