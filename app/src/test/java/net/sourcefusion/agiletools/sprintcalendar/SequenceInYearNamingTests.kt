package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.persisting.stubs.StubTeamRepository
import org.hamcrest.CoreMatchers.`is`
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.junit.Assert.assertThat
import org.junit.Test


class SequenceInYearNamingTests {

    val sequenceInYearNaming = SequenceInYearNaming()

    @Test
    fun namesAsYearDahSequence() {
        assertThat(nameOfSprintWithTodayOf("17.02.2016"), `is`("16-04"))
        assertThat(nameOfSprintWithTodayOf("31.03.2016"), `is`("16-07"))
        assertThat(nameOfSprintWithTodayOf("29.12.2015"), `is`("15-26"))
        assertThat(nameOfSprintWithTodayOf("05.01.2017"), `is`("17-01"))
    }

    private fun nameOfSprintWithTodayOf(today: String) = sequenceInYearNaming.nameFor(sprintCalendarWithTodayOf(today))

    private fun sprintCalendarWithTodayOf(today: String): SprintCalendar {
        return SprintCalendar(
                StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Pedro"))),
                TestDateProvider(today.toLocalDate(), LocalTime(9)),
                BasicHolidayProvider(LocalDate(2016, 1, 6)))
    }
}

