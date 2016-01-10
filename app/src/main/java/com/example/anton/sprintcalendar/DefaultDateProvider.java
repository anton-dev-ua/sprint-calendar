package com.example.anton.sprintcalendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

class DefaultDateProvider implements DateProvider {

    @Override
    public boolean isToday(LocalDate date) {
        return getToday().compareTo(date) == 0;
    }

    @Override
    public LocalDate getToday() {
        return new LocalDate();
    }
}
