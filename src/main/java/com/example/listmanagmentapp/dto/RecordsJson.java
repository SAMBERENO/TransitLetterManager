package com.example.listmanagmentapp.dto;

public record RecordsJson(
        String pudla,
        String nrWyrobu,
        String nrZlecenia,
        String dataOdbioru,
        int sumaUszczelek,
        int sumaBrakow,
        CategoryDamaged braki
) {

}
