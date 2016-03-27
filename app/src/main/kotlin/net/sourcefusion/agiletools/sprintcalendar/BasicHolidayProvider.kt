package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

import org.joda.time.DateTimeConstants.SATURDAY
import org.joda.time.DateTimeConstants.SUNDAY

open class BasicHolidayProvider(var holidaysList: List<LocalDate>) : HolidayProvider {
    constructor(vararg holidaysList: LocalDate): this(holidaysList.asList())

    override fun addHoliday(localDate: LocalDate) {
        holidaysList += localDate
    }

    override fun removeHoliday(localDate: LocalDate) {
        holidaysList -= localDate
    }

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
