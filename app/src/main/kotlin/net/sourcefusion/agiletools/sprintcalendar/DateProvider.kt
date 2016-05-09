package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate
import org.joda.time.LocalTime

interface DateProvider {
    fun isToday(date: LocalDate): Boolean

    val today: LocalDate

    val time: LocalTime
}
