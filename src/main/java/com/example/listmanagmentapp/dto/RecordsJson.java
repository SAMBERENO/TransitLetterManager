package com.example.listmanagmentapp.dto;

public record RecordsJson(
        char zmiana,

        String nrWyrobu,
        String nrZleceniaiPudla,
        String dataProdukcji,
        int sumaUszczelek,
        int sumaBrakow,
        int niezgodnosci,
        boolean kz,
        CategoryDamage braki
) {
}
