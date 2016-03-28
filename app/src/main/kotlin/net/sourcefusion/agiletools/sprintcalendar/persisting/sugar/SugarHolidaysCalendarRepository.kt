package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import net.sourcefusion.agiletools.sprintcalendar.persisting.HolidaysCalendarRepository
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

class SugarHolidaysCalendarRepository : HolidaysCalendarRepository {
    override fun saveHoliday(localDate: LocalDate) {
        val date = localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate()
        HolidayEntry(date).save()
    }

    override fun removeHoliday(localDate: LocalDate) {
        val date = localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate()
        HolidayEntry.findByDate(date)?.delete()
    }

    override fun readHolidays() = HolidayEntry.all().map { entry -> LocalDate(entry.holidayDate.time, DateTimeZone.UTC) }
}