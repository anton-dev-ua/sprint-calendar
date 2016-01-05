package com.example.anton.sprintcalendar;

import com.google.common.base.Preconditions;

import java.util.Calendar;
import java.util.Date;

public class SprintCalendar {

    private Date sprintStartDate;
    private Date lastSprintDate;
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

    public void initByStartDate(Date sprintStartDate) {
        calendar.setTime(sprintStartDate);
        Preconditions.checkArgument(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY, "Start date of sprint should be monday");
        this.sprintStartDate = sprintStartDate;
        calculateDates();
    }

    private void calculateDates() {
        day = new SprintDay[10];
        day[0] = new SprintDay(calendar.getTime(), false);
        for (int dayIndex = 0; dayIndex < 10; ) {
            if (isWorkingDay()) {
                day[dayIndex] = new SprintDay(calendar.getTime(), holidayProvider.isHoliday(calendar.getTime()));
                if (dateProvider.isToday(calendar.getTime())) {
                    today = day[dayIndex];
                }
                dayIndex++;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        lastSprintDate = calendar.getTime();
    }

    private boolean isWorkingDay() {
        return calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
    }

    public Date getSprintStartDate() {
        return sprintStartDate;
    }

    public Date getLastSprintDate() {
        return lastSprintDate;
    }

    public SprintDay[] getDay() {
        return day;
    }

    public SprintDay getToday() {
        return today;
    }

}
