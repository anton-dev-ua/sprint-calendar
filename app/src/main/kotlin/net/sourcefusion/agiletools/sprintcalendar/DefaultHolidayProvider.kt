package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

import java.util.Arrays
import java.util.Collections

import org.joda.time.DateTimeConstants.SATURDAY
import org.joda.time.DateTimeConstants.SUNDAY

class DefaultHolidayProvider(vararg holidaysList: LocalDate) : HolidayProvider {

    private val holidays: List<LocalDate>

    init {
        holidays = Arrays.asList(*holidaysList)
    }

    override fun isHoliday(date: LocalDate): Boolean {
        return isWeekend(date) || Collections.binarySearch(holidays, date) >= 0
    }

    override fun isWeekend(date: LocalDate): Boolean {
        return date.dayOfWeek == SATURDAY || date.dayOfWeek == SUNDAY
    }

    override fun isWorkingDay(localDate: LocalDate): Boolean {
        return !isHoliday(localDate)
    }


}
