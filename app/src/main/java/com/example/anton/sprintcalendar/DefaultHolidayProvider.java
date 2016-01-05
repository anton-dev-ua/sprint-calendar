package com.example.anton.sprintcalendar;

import org.joda.time.DateTimeComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DefaultHolidayProvider implements HolidayProvider {

    private static final DateTimeComparator dateComparator = DateTimeComparator.getDateOnlyInstance();
    private List<Date> holidays;

    public DefaultHolidayProvider(Date... holidaysList) {
        holidays = Arrays.asList(holidaysList);
    }

    @Override
    public boolean isHoliday(Date date) {
        return Collections.binarySearch(holidays, date, new DateComparator()) == 0;
    }

    private static class DateComparator implements Comparator<Date> {
        @Override
        public int compare(Date lhs, Date rhs) {
            return dateComparator.compare(lhs, rhs);
        }
    }
}
