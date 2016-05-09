package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate
import org.joda.time.LocalTime

class TestDateProvider(private val todayDate: LocalDate, private val currentTime: LocalTime) : DateProvider {

    override fun isToday(date: LocalDate): Boolean {
        return todayDate.compareTo(date) == 0
    }

    override val today: LocalDate
        get() = todayDate

    override val time: LocalTime
        get() = currentTime
}