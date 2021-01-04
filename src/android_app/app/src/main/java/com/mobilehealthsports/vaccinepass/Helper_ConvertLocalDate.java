package com.mobilehealthsports.vaccinepass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Helper_ConvertLocalDate {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.M.yyyy");


        System.out.println(LocalDate.parse("03.01.2022", formatter).toEpochDay());
        System.out.println(LocalDate.parse("03.01.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("09.10.2025", formatter).toEpochDay());
        System.out.println(LocalDate.parse("09.10.2020", formatter).toEpochDay());
        System.out.println(LocalDate.parse("20.07.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("20.01.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("28.01.2023", formatter).toEpochDay());
        System.out.println(LocalDate.parse("28.01.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("31.01.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("10.02.2022", formatter).toEpochDay());
        System.out.println(LocalDate.parse("10.02.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("18.01.2023", formatter).toEpochDay());
        System.out.println(LocalDate.parse("18.01.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("29.01.2026", formatter).toEpochDay());
        System.out.println(LocalDate.parse("29.01.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("17.08.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("17.02.2021", formatter).toEpochDay());
        System.out.println(LocalDate.parse("12.12.2022", formatter).toEpochDay());
        System.out.println(LocalDate.parse("12.12.2020", formatter).toEpochDay());


    }
}
