package com.example.anton.sprintcalendar;

import org.joda.time.ReadablePartial;

import java.util.Date;

public interface DateProvider {
    boolean isToday(Date date);

    ReadablePartial getToday();
}
