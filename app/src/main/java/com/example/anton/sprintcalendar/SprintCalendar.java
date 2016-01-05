package com.example.anton.sprintcalendar;

import com.google.common.base.Preconditions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SprintCalendar {

    private Date sprintStartDate;
    private Date lastSprintDate;
    private SprintDay[] day;
    private Calendar calendar = Calendar.getInstance();

    public void initByStartDate(Date sprintStartDate) {
        calendar.setTime(sprintStartDate);
        Preconditions.checkArgument(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY, "Start date of sprint should be monday");
        this.sprintStartDate = sprintStartDate;
        calculateDates();
    }

    private void calculateDates() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        day = new SprintDay[10];
        day[0] = new SprintDay(formatter.format(calendar.getTime()), false);
        for (int day = 1; day < 5; day++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            this.day[day] = new SprintDay(formatter.format(calendar.getTime()), false);
        }
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        for (int day = 5; day < 10; day++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            this.day[day] = new SprintDay(formatter.format(calendar.getTime()), false);
        }
        lastSprintDate = calendar.getTime();
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

}
