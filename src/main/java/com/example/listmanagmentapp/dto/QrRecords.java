package com.example.listmanagmentapp.dto;

public record QrRecords(
        char zmiana,
        String nrWyrobu,
        String nrZleceniaiPudla,
        String dataProdukcji,
        int sumaUszczelek
) {
}
