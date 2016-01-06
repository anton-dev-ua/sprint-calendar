package com.example.anton.sprintcalendar;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.SUNDAY;

public class DefaultHolidayProvider implements HolidayProvider {

    private List<LocalDate> holidays;

    public DefaultHolidayProvider(LocalDate... holidaysList) {
        holidays = Arrays.asList(holidaysList);
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return isWeekend(date) || Collections.binarySearch(holidays, date) >= 0;
    }

    @Override
    public boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == SATURDAY || date.getDayOfWeek() == SUNDAY;
    }

    @Override
    public boolean isWorkingDay(LocalDate localDate) {
        return !isHoliday(localDate);
    }


}
