package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

object Format {
    private val DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy")
    @JvmStatic fun date(date: LocalDate?): String {
        return if (date != null) DATE_FORMATTER.print(date) else "<null>"
    }
}
