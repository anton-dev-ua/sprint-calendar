package com.example.anton.sprintcalendar;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Format {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy");

    public static String date(LocalDate date) {
        return date != null ? DATE_FORMATTER.print(date) : "<null>";
    }
}
