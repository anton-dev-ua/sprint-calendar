package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

internal class DefaultDateProvider : DateProvider {

    override fun isToday(date: LocalDate): Boolean {
        return today.compareTo(date) == 0
    }

    override val today: LocalDate
        get() = LocalDate()
}
