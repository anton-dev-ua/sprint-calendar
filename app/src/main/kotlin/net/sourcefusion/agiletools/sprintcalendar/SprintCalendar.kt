package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.Days
import org.joda.time.LocalDate
import kotlin.properties.Delegates

class SprintCalendar(val team: Team, private val dateProvider: DateProvider, private val holidayProvider: HolidayProvider) {

    private val sprintBaseDate = LocalDate(2015, 12, 7)

    var firstDate by Delegates.notNull<LocalDate>()
        private set
    var lastDate by Delegates.notNull<LocalDate>()
        private set
    private var day = arrayListOf<SprintDay>()
    private var notifyMemberDayChange: (TeamMember, SprintDay) -> Unit = { a, b -> }
    private var notifyDayChange: (SprintDay) -> Unit = {a ->}


    init {
        initByCurrentDate()
    }

    fun initByCurrentDate() {
        val today = dateProvider.today
        var modDiff = Days.daysBetween(sprintBaseDate, today).days % 14
        if (modDiff < 0) modDiff += 14
        val startDate = today.minusDays(modDiff)
        initByStartDate(startDate)
    }

    private fun initByStartDate(sprintStartDate: LocalDate) {
        calculateDates(sprintStartDate)
    }

    fun day(index: Int): SprintDay {
        return if (index < day.size) day[index] else DAY_PLACEHOLDER
    }

    val daysLeft: Int
        get() = daysBetween(dateProvider.today)

    val totalDays: Int
        get() = daysBetween(firstDate)

    val totalHours: Int
        get() = hoursBetween(firstDate).toInt()

    val hoursLeft: Int
        get() = hoursBetween(dateProvider.today).toInt()

    private fun calculateDates(sprintStartDate: LocalDate) {
        day.clear()
        firstDate = sprintStartDate
        lastDate = firstDate + 11
        var date: LocalDate = firstDate
        var dayIndex = 0
        while (date.compareTo(lastDate) <= 0) {
            if (!holidayProvider.isWeekend(date)) {
                day.add(SprintDay(date, dayIndex++, holidayProvider.isHoliday(date), dateProvider.isToday(date)))
            }
            date += 1;
        }
    }

    private fun daysBetween(startDate: LocalDate?): Int {
        var days = 0
        for (sprintDay in day) {
            if (!sprintDay.isHoliday && sprintDay.date.compareTo(startDate) >= 0) {
                days++
            }
        }
        return days
    }

    private fun hoursBetween(startDate: LocalDate?): Float {
        var totalHours = 0f
        for (sprintDay in day) {
            if (!sprintDay.isHoliday && sprintDay.date.compareTo(startDate) >= 0) {
                for (member in team) {
                    totalHours += member.presence(sprintDay.date).hours
                }
            }
        }
        return totalHours
    }

    fun onMemberDay(member: TeamMember, day: SprintDay): Boolean {
        member.setPresence(day.date, if (member.presence(day.date) === PresenceType.FULL_DAY) PresenceType.NONE else PresenceType.FULL_DAY)
        notifyMemberDayChange(member, day)
        return true
    }

    fun onDay(sDay: SprintDay): Boolean {
        sDay.isHoliday = !sDay.isHoliday
        notifyDayChange(sDay)
        return false
    }

    fun onMemberDayChange(callback: (TeamMember, SprintDay) -> Unit):SprintCalendar {
        this.notifyMemberDayChange = callback
        return this
    }

    fun onDayChange(function: (SprintDay) -> Unit): SprintCalendar {
        notifyDayChange = function
        return this
    }
    companion object {
        val DAY_PLACEHOLDER = SprintDay(LocalDate(1970, 1, 1), 0, false, false)

    }
}

operator fun LocalDate.plus(days: Int) = this.plusDays(days)
