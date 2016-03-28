package net.sourcefusion.agiletools.sprintcalendar


import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.junit.Test

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

class BasicHolidayProviderTest {

    @Test
    fun detectsHoliday() {
        val basicHolidayProvider = BasicHolidayProvider(LocalDate(2016, 1, 6))

        assertThat(basicHolidayProvider.isHoliday(LocalDate(2016, 1, 6)), `is`(true))
        assertThat(basicHolidayProvider.isWorkingDay(LocalDate(2016, 1, 6)), `is`(false))
        assertThat(basicHolidayProvider.isHoliday(LocalDate(2016, 1, 5)), `is`(false))
        assertThat(basicHolidayProvider.isWorkingDay(LocalDate(2016, 1, 5)), `is`(true))
    }

    @Test
    fun detectsHolidayIgnoringTime() {
        val basicHolidayProvider = BasicHolidayProvider(DateTime(2016, 1, 6, 12, 1).toLocalDate())

        assertThat(basicHolidayProvider.isHoliday(DateTime(2016, 1, 6, 10, 25).toLocalDate()), `is`(true))
    }

    @Test
    fun detectsWeekends() {
        val basicHolidayProvider = BasicHolidayProvider()

        assertThat(basicHolidayProvider.isWeekend(LocalDate(2016, 1, 9)), `is`(true))
        assertThat(basicHolidayProvider.isWeekend(LocalDate(2016, 1, 10)), `is`(true))
    }

    @Test
    fun treatsWeekandsAsHolidays() {
        val basicHolidayProvider = BasicHolidayProvider()

        assertThat(basicHolidayProvider.isHoliday(LocalDate(2016, 1, 9)), `is`(true))
        assertThat(basicHolidayProvider.isHoliday(LocalDate(2016, 1, 10)), `is`(true))
    }

    @Test
    fun doesNotTreatsHolidayAsWeekend() {
        val basicHolidayProvider = BasicHolidayProvider(LocalDate(2016, 1, 6))

        assertThat(basicHolidayProvider.isHoliday(LocalDate(2016, 1, 6)), `is`(true))
        assertThat(basicHolidayProvider.isWeekend(LocalDate(2016, 1, 6)), `is`(false))
    }
}