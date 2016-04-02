package net.sourcefusion.agiletools.sprintcalendar.persisting

import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.*

object PersistingUtils {
    fun toDate(localDate: LocalDate) = localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate()
    fun toLocalDate(date: Date?) = LocalDate(date?.time, DateTimeZone.UTC)
}