package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

import org.joda.time.DateTimeConstants.SATURDAY
import org.joda.time.DateTimeConstants.SUNDAY

class DefaultHolidayProvider(vararg val holidaysList: LocalDate) : HolidayProvider {

    override fun isHoliday(date: LocalDate): Boolean {
        return isWeekend(date) || date in holidaysList
    }

    override fun isWeekend(date: LocalDate): Boolean {
        return date.dayOfWeek == SATURDAY || date.dayOfWeek == SUNDAY
    }

    override fun isWorkingDay(localDate: LocalDate): Boolean {
        return !isHoliday(localDate)
    }


}
