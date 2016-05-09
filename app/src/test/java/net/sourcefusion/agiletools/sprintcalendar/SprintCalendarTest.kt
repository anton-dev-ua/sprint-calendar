package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.persisting.stubs.StubTeamRepository
import org.hamcrest.CoreMatchers.`is`
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.junit.Assert.assertThat
import org.junit.Test
import java.text.ParseException

class SprintCalendarTest {
    private val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")

    @Test
    fun populatesDatesOfTenDaysOfSprint() {

        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider(date(TODAY_DATE), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.day(0).date, `is`("04.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(1).date, `is`("05.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(2).date, `is`("06.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(3).date, `is`("07.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(4).date, `is`("08.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(5).date, `is`("11.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(6).date, `is`("12.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(7).date, `is`("13.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(8).date, `is`("14.01.2016".toLocalDate()))
        assertThat(sprintCalendar.day(9).date, `is`("15.01.2016".toLocalDate()))
    }

    @Test
    @Throws(ParseException::class)
    fun determinesTodaySprintDay() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.day(6).isToday, `is`(true))
        assertThat(sprintCalendar.day(5).isToday, `is`(false))
    }

    @Test
    fun determinesHoliday() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.day(2).isHoliday, `is`(true))
    }

    @Test
    fun calculatesLeftSprintDays() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider())

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.daysLeft, `is`(4))
    }

    @Test
    fun calculatesLeftSprintDaysIgnoringWeekEnds() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("06.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider())

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.daysLeft, `is`(8))
    }

    @Test
    fun calculatesLeftSprintDaysIgnoringHolidays() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 14)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.daysLeft, `is`(3))
    }

    @Test
    fun calculatesTotalSprintDaysIgnoringHolidays() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team()), TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.totalDays, `is`(9))
    }

    @Test
    fun calculatesTotalSprintHours() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Pedro"))),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.totalHours, `is`(9 * 3 * 5))
    }

    @Test
    fun calculatesTotalSprintHoursExcludingAbsenceOfTeamMembers() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 14), PresenceType.ABSENT)
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, TeamMember("Pedro"))),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.totalHours, `is`(9 * 3 * 5 - 5))
    }

    @Test
    fun calculatesLeftSprintHours() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Pedro"))),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5))
    }

    @Test
    fun calculatesLeftSprintHoursExcludingAbsenceOfTeamMembers() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 14), PresenceType.ABSENT)
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, TeamMember("Pedro"))),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(9, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5 - 5))
    }

    @Test
    fun calculatesLeftSprintHoursExcludingPastHoursOfTheDay() {
        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Pedro"))),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(11, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5 - 4))
    }

    @Test
    fun calculatesLeftSprintHoursExcludingPastHoursOfTheDayOnlyForPresentMembers() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 12), PresenceType.ABSENT)

        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, TeamMember("Pedro"))),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(11, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5 - 5 - 3))
    }

    @Test
    fun calculatesLeftSprintHoursExcludingMorningPastHoursOfTheDayOnlyForPresentMembersInTheMorning() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 12), PresenceType.HALF_DAY)
        val pedro = TeamMember("Pedro")
        pedro.setPresence(LocalDate(2016, 1, 12), PresenceType.HALF_DAY_MORNING)

        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, pedro)),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(11, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5 - 5 - 3))
    }

    @Test
    fun calculatesLeftSprintHoursExcludingEveningPastHoursOfTheDayOnlyForPresentMembersInTheEvening() {
        val peter = TeamMember("Peter")
        peter.setPresence(LocalDate(2016, 1, 12), PresenceType.HALF_DAY)
        val pedro = TeamMember("Pedro")
        pedro.setPresence(LocalDate(2016, 1, 12), PresenceType.HALF_DAY_MORNING)

        val sprintCalendar = SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), peter, pedro)),
                TestDateProvider("12.01.2016".toLocalDate(), LocalTime(15, 0)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))

        sprintCalendar.initByCurrentDate()

        assertThat(sprintCalendar.hoursLeft, `is`(4 * 3 * 5 - 13))
    }

    private fun date(dateString: String): LocalDate {
        return formatter.parseDateTime(dateString).toLocalDate()
    }

    companion object {
        val TODAY_DATE = "12.01.2016"
    }

}
