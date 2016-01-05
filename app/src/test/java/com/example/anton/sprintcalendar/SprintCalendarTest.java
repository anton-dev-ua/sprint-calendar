package com.example.anton.sprintcalendar;

import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SprintCalendarTest {

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Test
    public void populatesDatesOfTenDaysOfSprint() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("12.01.2016"),
                new TestHolidayProvider("06.01.2016")
        );

        sprintCalendar.initByStartDate(date("04.01.2016"));

        assertThat(sprintCalendar.getDay()[0].getDate(), is("04.01.2016"));
        assertThat(sprintCalendar.getDay()[1].getDate(), is("05.01.2016"));
        assertThat(sprintCalendar.getDay()[2].getDate(), is("06.01.2016"));
        assertThat(sprintCalendar.getDay()[3].getDate(), is("07.01.2016"));
        assertThat(sprintCalendar.getDay()[4].getDate(), is("08.01.2016"));
        assertThat(sprintCalendar.getDay()[5].getDate(), is("11.01.2016"));
        assertThat(sprintCalendar.getDay()[6].getDate(), is("12.01.2016"));
        assertThat(sprintCalendar.getDay()[7].getDate(), is("13.01.2016"));
        assertThat(sprintCalendar.getDay()[8].getDate(), is("14.01.2016"));
        assertThat(sprintCalendar.getDay()[9].getDate(), is("15.01.2016"));
    }

    @Test
    public void determinesTodaySprintDay() throws ParseException {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new TestDateProvider("12.01.2016"),
                new TestHolidayProvider("06.01.2016")
        );

        sprintCalendar.initByStartDate(date("04.01.2016"));

        assertThat(sprintCalendar.getToday().getDate(), is("12.01.2016"));
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

    private Date date(String dateString) {
        return formatter.parseDateTime(dateString).toDate();
    }

    private class TestDateProvider implements DateProvider {

        private String todayDate;

        private TestDateProvider(String todayDate) {
            this.todayDate = todayDate;
        }

        @Override
        public boolean isToday(Date date) {
            return DateTimeComparator.getDateOnlyInstance().compare(date(todayDate), date) == 0;
        }
    }

    private class TestHolidayProvider implements HolidayProvider {

        private String dateString;

        private TestHolidayProvider(String dateString) {
            this.dateString = dateString;
        }

        @Override
        public boolean isHoliday(Date date) {
            return DateTimeComparator.getDateOnlyInstance().compare(date(dateString), date) == 0;

        }
    }
}
