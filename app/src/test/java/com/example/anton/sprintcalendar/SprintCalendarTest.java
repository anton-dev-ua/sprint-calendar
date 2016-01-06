package com.example.anton.sprintcalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SprintCalendarTest {

    public static final String TODAY_DATE = "12.01.2016";
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Test
    public void populatesDatesOfTenDaysOfSprint() {

        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider(TODAY_DATE),
                new TestHolidayProvider("06.01.2016")
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDay()[0].getDate(), is(date("04.01.2016")));
        assertThat(sprintCalendar.getDay()[1].getDate(), is(date("05.01.2016")));
        assertThat(sprintCalendar.getDay()[2].getDate(), is(date("06.01.2016")));
        assertThat(sprintCalendar.getDay()[3].getDate(), is(date("07.01.2016")));
        assertThat(sprintCalendar.getDay()[4].getDate(), is(date("08.01.2016")));
        assertThat(sprintCalendar.getDay()[5].getDate(), is(date("11.01.2016")));
        assertThat(sprintCalendar.getDay()[6].getDate(), is(date("12.01.2016")));
        assertThat(sprintCalendar.getDay()[7].getDate(), is(date("13.01.2016")));
        assertThat(sprintCalendar.getDay()[8].getDate(), is(date("14.01.2016")));
        assertThat(sprintCalendar.getDay()[9].getDate(), is(date("15.01.2016")));
    }

    @Test
    public void determinesTodaySprintDay() throws ParseException {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("12.01.2016"),
                new TestHolidayProvider("06.01.2016")
        );

        sprintCalendar.initByStartDate(date("04.01.2016"));

        assertThat(sprintCalendar.getToday().getDate(), is(date("12.01.2016")));
    }

    @Test
    public void determinesHoliday() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("12.01.2016"),
                new TestHolidayProvider("06.01.2016")
        );

        sprintCalendar.initByStartDate(date("04.01.2016"));

        assertThat(sprintCalendar.getDay()[2].isHoliday(), is(true));
    }

    @Test
    public void calculatesLeftSprintDays() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("12.01.2016"),
                new TestHolidayProvider()
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDaysLeft(), is(4));
    }

    @Test
    public void calculatesLeftSprintDaysIgnoringWeekEnds() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("06.01.2016"),
                new TestHolidayProvider()
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDaysLeft(), is(8));
    }

    @Test
    public void calculatesLeftSprintDaysIgnoringHolidays() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("12.01.2016"),
                new TestHolidayProvider("14.01.2016")
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDaysLeft(), is(3));
    }

    private LocalDate date(String dateString) {
        return formatter.parseDateTime(dateString).toLocalDate();
    }

    private class TestDateProvider implements DateProvider {

        private String todayDate;

        private TestDateProvider(String todayDate) {
            this.todayDate = todayDate;
        }

        @Override
        public boolean isToday(LocalDate date) {
            return date(todayDate).compareTo(date) == 0;
        }

        @Override
        public LocalDate getToday() {
            return date(todayDate);
        }
    }

    private class TestHolidayProvider implements HolidayProvider {

        private String[] dateStrings;

        private TestHolidayProvider(String... dateString) {
            this.dateStrings = dateString;
        }

        @Override
        public boolean isHoliday(LocalDate date) {
            for (String dateString : dateStrings) {
                if (date(dateString).compareTo(date) == 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
