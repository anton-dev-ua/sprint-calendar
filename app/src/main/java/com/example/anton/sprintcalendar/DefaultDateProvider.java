package com.example.anton.sprintcalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.ReadablePartial;

import java.util.Date;

class DefaultDateProvider implements DateProvider {

    private static final DateTimeComparator dateComparator = DateTimeComparator.getDateOnlyInstance();

    @Override
    public boolean isToday(Date date) {
        return dateComparator.compare(date, new Date()) == 0;
    }

    @Override
    public ReadablePartial getToday() {
        return new DateTime().toLocalDate();
    }
}
