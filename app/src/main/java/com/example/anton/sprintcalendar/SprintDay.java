package com.example.anton.sprintcalendar;

public class SprintDay {

    private String date;
    private boolean holiday;

    public SprintDay(String date, boolean holiday) {
        this.date = date;
        this.holiday = holiday;
    }

    public String getDate() {
        return date;
    }

    public boolean isHoliday() {
        return holiday;
    }
}
