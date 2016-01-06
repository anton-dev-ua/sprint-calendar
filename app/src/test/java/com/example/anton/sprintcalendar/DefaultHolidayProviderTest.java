package com.example.anton.sprintcalendar;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultHolidayProviderTest {

    @Test
    public void detectsHoliday() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider(new LocalDate(2016, 1, 6));

        assertThat(defaultHolidayProvider.isHoliday(new LocalDate(2016, 1, 6)), is(true));
        assertThat(defaultHolidayProvider.isHoliday(new LocalDate(2016, 1, 5)), is(false));
    }

    @Test
    public void detectsHolidayIgnoringTime() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider(new DateTime(2016, 1, 6, 12, 1).toLocalDate());

        assertThat(defaultHolidayProvider.isHoliday(new DateTime(2016, 1, 6, 10, 25).toLocalDate()), is(true));
    }
}