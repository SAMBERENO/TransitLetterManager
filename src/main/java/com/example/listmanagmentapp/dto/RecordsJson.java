package com.example.listmanagmentapp.dto;

public record RecordsJson(
        String pudla,
        String nrWyrobu,
        String nrZlecenia,
        String dataOdbioru,
        int sumaUszczelek,
        int sumaBrakow,
        int niezgodnosci,
        boolean kz,
        CategoryDamage braki
) {

}
