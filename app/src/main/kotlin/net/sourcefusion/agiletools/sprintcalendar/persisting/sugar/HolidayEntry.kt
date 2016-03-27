package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import com.orm.SugarRecord
import com.orm.dsl.Unique
import java.util.*

// select date(holiday_date/1000, 'unixepoch') from holiday_entry;
class HolidayEntry(@Unique var holidayDate: Date) : SugarRecord() {
    constructor() : this(Date())

    companion object {
        fun all() = listAll(HolidayEntry::class.java)
        fun findByDate(date: Date): HolidayEntry? = find(HolidayEntry::class.java, "holiday_date=?", "${date.time}").firstOrNull()
    }
}