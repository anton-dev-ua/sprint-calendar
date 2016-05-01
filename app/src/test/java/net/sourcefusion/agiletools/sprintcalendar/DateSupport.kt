package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

//object DateSupport {
//    private val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")

fun String.toLocalDate(): LocalDate {
    return DateTimeFormat.forPattern("dd.MM.yyyy").parseDateTime(this).toLocalDate()
}
//}