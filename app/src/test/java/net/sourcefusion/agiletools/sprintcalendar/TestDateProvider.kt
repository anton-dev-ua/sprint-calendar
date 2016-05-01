package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

class TestDateProvider(private val todayDate: LocalDate) : DateProvider {

    override fun isToday(date: LocalDate): Boolean {
        return todayDate.compareTo(date) == 0
    }

    override val today: LocalDate
        get() = todayDate
}