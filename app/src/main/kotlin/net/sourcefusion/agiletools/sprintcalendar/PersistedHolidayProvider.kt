package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.persisting.SprintCalendarDao
import org.joda.time.LocalDate

class PersistedHolidayProvider(val sprintCalendarDao: SprintCalendarDao) : BasicHolidayProvider(sprintCalendarDao.readHolidays()) {

    override fun addHoliday(localDate: LocalDate) {
        super.addHoliday(localDate)
        sprintCalendarDao.saveHoliday(localDate);
    }

    override fun removeHoliday(localDate: LocalDate) {
        super.removeHoliday(localDate)
        sprintCalendarDao.removeHoliday(localDate)
    }
}