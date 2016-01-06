package com.example.anton.sprintcalendar;

import com.google.common.base.Objects;

import org.joda.time.LocalDate;

public class SprintDay {

    private LocalDate date;
    private boolean holiday;
    private boolean today;

    public SprintDay(LocalDate date, boolean holiday, boolean today) {
        this.date = date;
        this.holiday = holiday;
        this.today = today;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public boolean isToday() {
        return today;
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
