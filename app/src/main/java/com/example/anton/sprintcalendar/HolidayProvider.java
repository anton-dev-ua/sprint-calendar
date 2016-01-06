package com.example.anton.sprintcalendar;

import org.joda.time.LocalDate;

import java.util.Date;

public interface HolidayProvider {
    boolean isHoliday(LocalDate time);
}
