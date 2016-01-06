package com.example.anton.sprintcalendar;

import com.google.common.base.Preconditions;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;

import java.util.Calendar;

import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.SUNDAY;

public class SprintCalendar {

    LocalDate sprintBaseDate = new DateTime(2015, 12, 7, 0, 0).toLocalDate();


    private LocalDate firstSprintDate;
    private LocalDate lastSprintDate;
    private SprintDay[] day;
    private Calendar calendar = Calendar.getInstance();
    private SprintDay today;
    private DateProvider dateProvider;
    private HolidayProvider holidayProvider;

    public SprintCalendar(DateProvider dateProvider, HolidayProvider holidayProvider) {
        Preconditions.checkArgument(dateProvider != null, "date provider cannot be null");
        Preconditions.checkArgument(holidayProvider != null, "holiday provider cannot be null");
        this.dateProvider = dateProvider;
        this.holidayProvider = holidayProvider;
    }

    public void initByStartDate(LocalDate sprintStartDate) {
        calendar.setTime(sprintStartDate.toDate());
        Preconditions.checkArgument(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY, "Start date of sprint should be monday");
        calculateDates();
    }

    private void calculateDates() {
        day = new SprintDay[10];
        day[0] = new SprintDay(new LocalDate(calendar.getTime()), false);
        firstSprintDate = day[0].getDate();
        for (int dayIndex = 0; dayIndex < 10; ) {
            if (isWorkingDay()) {
                day[dayIndex] = new SprintDay(new LocalDate(calendar.getTime()), holidayProvider.isHoliday(new LocalDate(calendar.getTime())));
                if (dateProvider.isToday(new LocalDate(calendar.getTime()))) {
                    today = day[dayIndex];
                }
                dayIndex++;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        lastSprintDate = day[9].getDate();
    }

    private boolean isWorkingDay() {
        return calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
    }

    public LocalDate getFirstDate() {
        return firstSprintDate;
    }

    public LocalDate getLastDate() {
        return lastSprintDate;
    }

    public SprintDay[] getDay() {
        return day;
    }

    public SprintDay getToday() {
        return today;
    }

    public void initByCurrentDate() {
        LocalDate startDate = sprintBaseDate.plusWeeks((Weeks.weeksBetween(sprintBaseDate, dateProvider.getToday()).getWeeks() / 2) * 2);
        initByStartDate(startDate);
    }

    public int getDaysLeft() {
        return daysBetween(dateProvider.getToday(), lastSprintDate);
    }

    public int getTotalDays() {
        return daysBetween(firstSprintDate, lastSprintDate);
    }

    private int daysBetween(LocalDate startDate, LocalDate endDate) {
        int days=0;
        for(LocalDate date = startDate; date.compareTo(endDate)<=0; date = date.plusDays(1)) {
            if(date.getDayOfWeek() != SUNDAY && date.getDayOfWeek() != SATURDAY && !holidayProvider.isHoliday(date)) {
                days++;
            }
        }
        return days;
    }
}
