package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import net.sourcefusion.agiletools.sprintcalendar.persisting.HolidaysCalendarRepository
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils.toDate
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils.toLocalDate
import org.joda.time.LocalDate

class SugarHolidaysCalendarRepository : HolidaysCalendarRepository {
    override fun saveHoliday(localDate: LocalDate) {
        HolidayEntry(toDate(localDate)).save()
    }

    override fun removeHoliday(localDate: LocalDate) {
        HolidayEntry.findByDate(toDate(localDate))?.delete()
    }

    override fun readHolidays() = HolidayEntry.all().map { entry -> toLocalDate(entry.holidayDate) }
}