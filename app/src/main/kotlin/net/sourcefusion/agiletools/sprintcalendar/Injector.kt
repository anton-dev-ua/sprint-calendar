package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

object Injector {
    var holidayProvider: HolidayProvider = DefaultHolidayProvider(LocalDate(2016, 1, 20))
}
