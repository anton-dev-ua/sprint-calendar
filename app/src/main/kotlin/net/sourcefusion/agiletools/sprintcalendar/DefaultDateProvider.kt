package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate
import org.joda.time.LocalTime

internal class DefaultDateProvider : DateProvider {
    override fun isToday(date: LocalDate): Boolean {
        return today.compareTo(date) == 0
    }

    override val today: LocalDate
        get() = LocalDate()

    override val time: LocalTime
        get() = LocalTime()
}
