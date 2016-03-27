package net.sourcefusion.agiletools.sprintcalendar.persisting

import org.joda.time.LocalDate

interface SprintCalendarDao {
    fun readHolidays(): List<LocalDate>
    fun saveHoliday(localDate: LocalDate)
    fun removeHoliday(localDate: LocalDate)
}