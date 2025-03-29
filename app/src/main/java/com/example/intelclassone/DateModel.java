package com.example.intelclassone;

public class DateModel {
    private String day;
    private String monthYear;

    public DateModel(String day, String monthYear) {
        this.day = day;
        this.monthYear = monthYear;
    }

    public String getDay() {
        return day;
    }

    public String getMonthYear() {
        return monthYear;
    }
}

