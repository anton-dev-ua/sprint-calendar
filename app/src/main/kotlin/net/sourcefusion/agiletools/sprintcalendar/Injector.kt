package net.sourcefusion.agiletools.sprintcalendar

object Injector {
    var holidayProvider: HolidayProvider = BasicHolidayProvider()
    var dateProvider: DateProvider = DefaultDateProvider()
}
