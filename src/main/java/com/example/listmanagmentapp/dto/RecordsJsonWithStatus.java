package com.example.listmanagmentapp.dto;

public record RecordsJsonWithStatus(
        RecordsJson recordsJson,
        int zatwierdzone
) {
}
