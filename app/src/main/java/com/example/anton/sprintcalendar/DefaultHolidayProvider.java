package com.example.anton.sprintcalendar;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DefaultHolidayProvider implements HolidayProvider {

    private static final DateTimeComparator dateComparator = DateTimeComparator.getDateOnlyInstance();
    private List<LocalDate> holidays;

    public DefaultHolidayProvider(LocalDate... holidaysList) {
        holidays = Arrays.asList(holidaysList);
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return Collections.binarySearch(holidays, date) >= 0;
    }
}
