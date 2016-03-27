package net.sourcefusion.agiletools.sprintcalendar;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BasicHolidayProviderTest {

    @Test
    public void detectsHoliday() {
        BasicHolidayProvider basicHolidayProvider = new BasicHolidayProvider(new LocalDate(2016, 1, 6));

        assertThat(basicHolidayProvider.isHoliday(new LocalDate(2016, 1, 6)), is(true));
        assertThat(basicHolidayProvider.isWorkingDay(new LocalDate(2016, 1, 6)), is(false));
        assertThat(basicHolidayProvider.isHoliday(new LocalDate(2016, 1, 5)), is(false));
        assertThat(basicHolidayProvider.isWorkingDay(new LocalDate(2016, 1, 5)), is(true));
    }

    @Test
    public void detectsHolidayIgnoringTime() {
        BasicHolidayProvider basicHolidayProvider = new BasicHolidayProvider(new DateTime(2016, 1, 6, 12, 1).toLocalDate());

        assertThat(basicHolidayProvider.isHoliday(new DateTime(2016, 1, 6, 10, 25).toLocalDate()), is(true));
    }

    @Test
    public void detectsWeekends() {
        BasicHolidayProvider basicHolidayProvider = new BasicHolidayProvider();

        assertThat(basicHolidayProvider.isWeekend(new LocalDate(2016, 1, 9)), is(true));
        assertThat(basicHolidayProvider.isWeekend(new LocalDate(2016, 1, 10)), is(true));
    }

    @Test
    public void treatsWeekandsAsHolidays() {
        BasicHolidayProvider basicHolidayProvider = new BasicHolidayProvider();

        assertThat(basicHolidayProvider.isHoliday(new LocalDate(2016, 1, 9)), is(true));
        assertThat(basicHolidayProvider.isHoliday(new LocalDate(2016, 1, 10)), is(true));
    }

    @Test
    public void doesNotTreatsHolidayAsWeekend() {
        BasicHolidayProvider basicHolidayProvider = new BasicHolidayProvider(new LocalDate(2016, 1, 6));

        assertThat(basicHolidayProvider.isHoliday(new LocalDate(2016, 1, 6)), is(true));
        assertThat(basicHolidayProvider.isWeekend(new LocalDate(2016, 1, 6)), is(false));
    }
}