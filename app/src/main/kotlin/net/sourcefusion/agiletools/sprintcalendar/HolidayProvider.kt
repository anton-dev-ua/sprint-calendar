package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

interface HolidayProvider {

    fun isWeekend(date: LocalDate): Boolean
    fun isHoliday(time: LocalDate): Boolean
    fun isWorkingDay(localDate: LocalDate): Boolean
}
