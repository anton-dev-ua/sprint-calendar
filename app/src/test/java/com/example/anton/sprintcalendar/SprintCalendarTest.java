package com.example.anton.sprintcalendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SprintCalendarTest {

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Test
    public void populatesDatesOfTenDaysOfSprint() throws ParseException {
        SprintCalendar sprintCalendar = new SprintCalendar();

        sprintCalendar.initByStartDate(date("04.01.2016"));

        assertThat(sprintCalendar.getDays()[0], is("04.01.2016"));
        assertThat(sprintCalendar.getDays()[1], is("05.01.2016"));
        assertThat(sprintCalendar.getDays()[2], is("06.01.2016"));
        assertThat(sprintCalendar.getDays()[3], is("07.01.2016"));
        assertThat(sprintCalendar.getDays()[4], is("08.01.2016"));
        assertThat(sprintCalendar.getDays()[5], is("11.01.2016"));
        assertThat(sprintCalendar.getDays()[6], is("12.01.2016"));
        assertThat(sprintCalendar.getDays()[7], is("13.01.2016"));
        assertThat(sprintCalendar.getDays()[8], is("14.01.2016"));
        assertThat(sprintCalendar.getDays()[9], is("15.01.2016"));
    }

    private Date date(String dateString) throws ParseException {
        return formatter.parseDateTime(dateString).toDate();
    }
}
