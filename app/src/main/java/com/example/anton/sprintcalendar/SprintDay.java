package com.example.anton.sprintcalendar;

import com.google.common.base.Objects;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class SprintDay {

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    private LocalDate date;
    private boolean holiday;

    public SprintDay(LocalDate date, boolean holiday) {
        this.date = date;
        this.holiday = holiday;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isHoliday() {
        return holiday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SprintDay sprintDay = (SprintDay) o;
        return Objects.equal(date, sprintDay.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }
}
