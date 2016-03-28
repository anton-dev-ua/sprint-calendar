package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.persisting.stubs.StubTeamRepository
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.junit.Test

import java.text.ParseException

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

class SprintCalendarTest {
    private val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")

    @Test
    fun populatesDatesOfTenDaysOfSprint() {

        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider(TODAY_DATE),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.day(0).date, `is`(date("04.01.2016")))
        assertThat(sprintCalendar.day(1).date, `is`(date("05.01.2016")))
        assertThat(sprintCalendar.day(2).date, `is`(date("06.01.2016")))
        assertThat(sprintCalendar.day(3).date, `is`(date("07.01.2016")))
        assertThat(sprintCalendar.day(4).date, `is`(date("08.01.2016")))
        assertThat(sprintCalendar.day(5).date, `is`(date("11.01.2016")))
        assertThat(sprintCalendar.day(6).date, `is`(date("12.01.2016")))
        assertThat(sprintCalendar.day(7).date, `is`(date("13.01.2016")))
        assertThat(sprintCalendar.day(8).date, `is`(date("14.01.2016")))
        assertThat(sprintCalendar.day(9).date, `is`(date("15.01.2016")))
    }

    @Test
    @Throws(ParseException::class)
    fun determinesTodaySprintDay() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.day(6).isToday, `is`(true))
        assertThat(sprintCalendar.day(5).isToday, `is`(false))
    }

    @Test
    fun determinesHoliday() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.day(2).isHoliday, `is`(true))
    }

    @Test
    fun calculatesLeftSprintDays() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016"),
                BasicHolidayProvider())

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.daysLeft, `is`(4))
    }

    @Test
    fun calculatesLeftSprintDaysIgnoringWeekEnds() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("06.01.2016"),
                BasicHolidayProvider())

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.daysLeft, `is`(8))
    }

    @Test
    fun calculatesLeftSprintDaysIgnoringHolidays() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 14)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.daysLeft, `is`(3))
    }

    @Test
    fun calculatesTotalSprintDaysIgnoringHolidays() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.totalDays, `is`(9))
    }

    @Test
    fun calculatesTotalSprintHours() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Pedro"))),
                TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.totalHours, `is`(9 * 3 * 5))
    }

    @Test
    fun calculatesTotalSprintHoursExcludingAbsenceOfTeamMembers() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 14), PresenceType.NONE)
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, TeamMember("Pedro"))),
                TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.totalHours, `is`(9 * 3 * 5 - 5))
    }

    @Test
    fun calculatesLeftSprintHours() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Pedro"))),
                TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5))
    }

    @Test
    fun calculatesLeftSprintHoursExcludingAbsenceOfTeamMembers() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 14), PresenceType.NONE)
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, TeamMember("Pedro"))),
                TestDateProvider("12.01.2016"),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5 - 5))
    }

    private fun date(dateString: String): LocalDate {
        return formatter.parseDateTime(dateString).toLocalDate()
    }

    private inner class TestDateProvider(private val todayDate: String) : DateProvider {

        override fun isToday(date: LocalDate): Boolean {
            return date(todayDate).compareTo(date) == 0
        }

        override val today: LocalDate
            get() = date(todayDate)
    }

    companion object {

        val TODAY_DATE = "12.01.2016"
    }

}
