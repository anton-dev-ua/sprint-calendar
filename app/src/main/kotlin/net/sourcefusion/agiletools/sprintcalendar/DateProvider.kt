package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

interface DateProvider {
    fun isToday(date: LocalDate): Boolean

    val today: LocalDate
}
