package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.persisting.HolidaysCalendarRepository
import org.joda.time.LocalDate

class PersistedHolidayProvider(val holidaysCalendarRepository: HolidaysCalendarRepository) : BasicHolidayProvider(holidaysCalendarRepository.readHolidays()) {

    override fun addHoliday(localDate: LocalDate) {
        super.addHoliday(localDate)
        holidaysCalendarRepository.saveHoliday(localDate);
    }

    override fun removeHoliday(localDate: LocalDate) {
        super.removeHoliday(localDate)
        holidaysCalendarRepository.removeHoliday(localDate)
    }
}