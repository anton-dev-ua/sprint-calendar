package com.example.anton.sprintcalendar;

import java.util.Date;

public interface HolidayProvider {
    boolean isHoliday(Date time);
}
