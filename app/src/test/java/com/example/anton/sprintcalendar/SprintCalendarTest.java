package com.example.anton.sprintcalendar;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SprintCalendarTest {

    public static final String TODAY_DATE = "12.01.2016";
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Test
    public void populatesDatesOfTenDaysOfSprint() {

        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider(TODAY_DATE),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.day(0).getDate(), is(date("04.01.2016")));
        assertThat(sprintCalendar.day(1).getDate(), is(date("05.01.2016")));
        assertThat(sprintCalendar.day(2).getDate(), is(date("06.01.2016")));
        assertThat(sprintCalendar.day(3).getDate(), is(date("07.01.2016")));
        assertThat(sprintCalendar.day(4).getDate(), is(date("08.01.2016")));
        assertThat(sprintCalendar.day(5).getDate(), is(date("11.01.2016")));
        assertThat(sprintCalendar.day(6).getDate(), is(date("12.01.2016")));
        assertThat(sprintCalendar.day(7).getDate(), is(date("13.01.2016")));
        assertThat(sprintCalendar.day(8).getDate(), is(date("14.01.2016")));
        assertThat(sprintCalendar.day(9).getDate(), is(date("15.01.2016")));
    }

    @Test
    public void determinesTodaySprintDay() throws ParseException {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.day(6).isToday(), is(true));
        assertThat(sprintCalendar.day(5).isToday(), is(false));
    }

    @Test
    public void determinesHoliday() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.day(2).isHoliday(), is(true));
    }

    @Test
    public void calculatesLeftSprintDays() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider()
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDaysLeft(), is(4));
    }

    @Test
    public void calculatesLeftSprintDaysIgnoringWeekEnds() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider("06.01.2016"),
                new DefaultHolidayProvider()
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDaysLeft(), is(8));
    }

    @Test
    public void calculatesLeftSprintDaysIgnoringHolidays() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 14))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getDaysLeft(), is(3));
    }

    @Test
    public void calculatesTotalSprintDaysIgnoringHolidays() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(), new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getTotalDays(), is(9));
    }

    @Test
    public void calculatesTotalSprintHours() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(new TeamMember("John"), new TeamMember("Peter"), new TeamMember("Pedro")),
                new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getTotalHours(), is(9 * 3 * 5));
    }

    @Test
    public void calculatesTotalSprintHoursExcludingAbsenceOfTeamMembers() {
        TeamMember peter = new TeamMember("Peter");
        peter.setPresence(new LocalDate(2016, 01, 14), PresenceType.NONE);
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(new TeamMember("John"), peter, new TeamMember("Pedro")),
                new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getTotalHours(), is(9 * 3 * 5 - 5));
    }

    @Test
    public void calculatesLeftSprintHours() {
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(new TeamMember("John"), new TeamMember("Peter"), new TeamMember("Pedro")),
                new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getHoursLeft(), is(4 * 3 * 5));
    }

    @Test
    public void calculatesLeftSprintHoursExcludingAbsenceOfTeamMembers() {
        TeamMember peter = new TeamMember("Peter");
        peter.setPresence(new LocalDate(2016, 01, 14), PresenceType.NONE);
        SprintCalendar sprintCalendar = new SprintCalendar(
                new Team(new TeamMember("John"), peter, new TeamMember("Pedro")),
                new TestDateProvider("12.01.2016"),
                new DefaultHolidayProvider(new LocalDate(2016, 01, 06))
        );

        sprintCalendar.initByCurrentDate();

        assertThat(sprintCalendar.getHoursLeft(), is(4 * 3 * 5 - 5));
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

}
