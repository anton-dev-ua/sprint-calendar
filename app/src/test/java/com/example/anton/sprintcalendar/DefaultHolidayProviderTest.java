package com.example.anton.sprintcalendar;


import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultHolidayProviderTest {

    @Test
    public void detectsHoliday() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider(new DateTime(2016, 1, 6, 0, 0).toDate());

        assertThat(defaultHolidayProvider.isHoliday(new DateTime(2016, 1, 6, 0, 0).toDate()), is(true));
        assertThat(defaultHolidayProvider.isHoliday(new DateTime(2016, 1, 5, 0, 0).toDate()), is(false));
    }

    @Test
    public void detectsHolidayIgnoringTime() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider(new DateTime(2016, 1, 6, 12, 1).toDate());

        assertThat(defaultHolidayProvider.isHoliday(new DateTime(2016, 1, 6, 10, 25).toDate()), is(true));
    }
}