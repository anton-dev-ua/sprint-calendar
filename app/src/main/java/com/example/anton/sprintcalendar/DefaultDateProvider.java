package com.example.anton.sprintcalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.util.Date;

class DefaultDateProvider implements DateProvider {

    private static final DateTimeComparator dateComparator = DateTimeComparator.getDateOnlyInstance();

    @Override
    public boolean isToday(LocalDate date) {
        return new LocalDate().compareTo(date) == 0;
    }

    @Override
    public LocalDate getToday() {
        return new DateTime().toLocalDate();
    }
}
