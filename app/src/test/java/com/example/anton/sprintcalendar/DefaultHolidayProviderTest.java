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
        assertThat(defaultHolidayProvider.isWorkingDay(new LocalDate(2016, 1, 6)), is(false));
        assertThat(defaultHolidayProvider.isHoliday(new LocalDate(2016, 1, 5)), is(false));
        assertThat(defaultHolidayProvider.isWorkingDay(new LocalDate(2016, 1, 5)), is(true));
    }

    @Test
    public void detectsHolidayIgnoringTime() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider(new DateTime(2016, 1, 6, 12, 1).toLocalDate());

        assertThat(defaultHolidayProvider.isHoliday(new DateTime(2016, 1, 6, 10, 25).toLocalDate()), is(true));
    }

    @Test
    public void detectsWeekends() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider();

        assertThat(defaultHolidayProvider.isWeekend(new LocalDate(2016, 1, 9)), is(true));
        assertThat(defaultHolidayProvider.isWeekend(new LocalDate(2016, 1, 10)), is(true));
    }

    @Test
    public void treatsWeekandsAsHolidays() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider();

        assertThat(defaultHolidayProvider.isHoliday(new LocalDate(2016, 1, 9)), is(true));
        assertThat(defaultHolidayProvider.isHoliday(new LocalDate(2016, 1, 10)), is(true));
    }

    @Test
    public void doesNotTreatsHolidayAsWeekend() {
        DefaultHolidayProvider defaultHolidayProvider = new DefaultHolidayProvider(new LocalDate(2016, 1, 6));

        assertThat(defaultHolidayProvider.isHoliday(new LocalDate(2016, 1, 6)), is(true));
        assertThat(defaultHolidayProvider.isWeekend(new LocalDate(2016, 1, 6)), is(false));
    }
}