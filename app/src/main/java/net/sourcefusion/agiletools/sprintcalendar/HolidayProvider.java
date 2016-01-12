package net.sourcefusion.agiletools.sprintcalendar;

import org.joda.time.LocalDate;

public interface HolidayProvider {

    boolean isWeekend(LocalDate date);
    boolean isHoliday(LocalDate time);
    boolean isWorkingDay(LocalDate localDate);
}
