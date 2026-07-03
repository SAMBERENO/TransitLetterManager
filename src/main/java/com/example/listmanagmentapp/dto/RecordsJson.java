package com.example.listmanagmentapp.dto;

public record RecordsJson(
        char zmiana,
        String nrPudla,
        String nrWyrobu,
        String nrZlecenia,
        String dataProdukcji,
        int sumaUszczelek,
        int sumaBrakow,
        int niezgodnosci,
        boolean kz,
        CategoryDamage braki
) {
}
