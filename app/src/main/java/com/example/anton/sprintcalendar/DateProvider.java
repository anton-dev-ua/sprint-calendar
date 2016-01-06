package com.example.anton.sprintcalendar;

import org.joda.time.LocalDate;

import java.util.Date;

public interface DateProvider {
    boolean isToday(LocalDate date);

    LocalDate getToday();
}
