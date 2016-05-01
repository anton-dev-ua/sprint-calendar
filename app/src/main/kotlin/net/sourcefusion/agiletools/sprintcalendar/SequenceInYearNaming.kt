package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class SequenceInYearNaming : SprintNamingStrategy {
    override fun nameFor(sprintCalendar: SprintCalendar): String {
        val firstDate = sprintCalendar.firstDate
        val year = DateTimeFormat.forPattern("yy").print(firstDate)
        var startDate = LocalDate(firstDate.year, DateTimeConstants.JANUARY, 1);
        while (startDate.dayOfWeek != DateTimeConstants.MONDAY) startDate += 1;
        val sequence = (firstDate - startDate) / 14 + 1
        return "$year-%02d".format(sequence)
    }

}