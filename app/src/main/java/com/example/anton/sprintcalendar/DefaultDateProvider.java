package com.example.anton.sprintcalendar;

import org.joda.time.DateTimeComparator;

import java.util.Date;

class DefaultDateProvider implements DateProvider {

    private static final DateTimeComparator dateComparator = DateTimeComparator.getDateOnlyInstance();

    @Override
    public boolean isToday(Date date) {
        return dateComparator.compare(date, new Date()) == 0;
    }
}
