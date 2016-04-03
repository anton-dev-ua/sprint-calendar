package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.PresenceType.*
import net.sourcefusion.agiletools.sprintcalendar.persisting.TeamRepository
import org.joda.time.Days
import org.joda.time.LocalDate
import kotlin.properties.Delegates

class SprintCalendar(val teamRepository: TeamRepository, private val dateProvider: DateProvider, val holidayProvider: HolidayProvider) {

    private val sprintBaseDate = LocalDate(2015, 12, 7)
    val team by lazy {
        teamRepository.readTeam()
    }

    var firstDate by Delegates.notNull<LocalDate>()
        private set
    var lastDate by Delegates.notNull<LocalDate>()
        private set
    private var days = arrayListOf<SprintDay>()
    private var notifyMemberDayChange: (TeamMember, Int) -> Unit = { a, b -> }
    private var notifyMemberChange: (TeamMember) -> Unit = { a -> }
    private var notifyDayChange: (Int) -> Unit = { a -> }
    private var notifyFullRefresh: () -> Unit = { }

    private var cashedToday by Delegates.notNull<LocalDate>()

    var currentSprintFirstDate by Delegates.notNull<LocalDate>()
        private set

    init {
        initByCurrentDate()
    }


    fun initByCurrentDate() {
        val today = dateProvider.today
        cashedToday = today
        var modDiff = Days.daysBetween(sprintBaseDate, today).days % 14
        if (modDiff < 0) modDiff += 14
        val startDate = today.minusDays(modDiff)
        currentSprintFirstDate = if (Days.daysBetween(startDate, today).days > 11) startDate + 14 else startDate
        initByStartDate(currentSprintFirstDate)
    }

    private fun initByStartDate(sprintStartDate: LocalDate) {
        calculateDates(sprintStartDate)
    }

    fun day(index: Int): SprintDay {
        return if (index < days.size) days[index] else DAY_PLACEHOLDER
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
        days.clear()
        firstDate = sprintStartDate
        lastDate = firstDate + 11
        var date = firstDate
        while (date.compareTo(lastDate) <= 0) {
            if (!holidayProvider.isWeekend(date)) {
                days.add(SprintDay(date, holidayProvider.isHoliday(date), dateProvider.isToday(date)))
            }
            date += 1;
        }
    }

    private fun daysBetween(startDate: LocalDate): Int {
        var daysDif = 0
        for (sprintDay in days) {
            if (!sprintDay.isHoliday && sprintDay.date.compareTo(startDate) >= 0) {
                daysDif++
            }
        }
        return daysDif
    }

    private fun hoursBetween(startDate: LocalDate): Float {
        var totalHours = 0f
        for (sprintDay in days) {
            if (!sprintDay.isHoliday && sprintDay.date.compareTo(startDate) >= 0) {
                for (member in team) {
                    if (member == Team.TEAM_MEMBER_PLACEHOLDER) continue
                    totalHours += member.presence(sprintDay.date).hours
                }
            }
        }
        return totalHours
    }

    fun addTeamMember(name: String) {
        val teamMember = team.addTeamMember(name)
        teamRepository.saveTeamMember(teamMember)
    }

    fun onMemberDay(member: TeamMember, dayIndex: Int): Boolean {
        val day = day(dayIndex)
        member.setPresence(day.date, if (member.presence(day.date) === FULL_DAY) ABSENT else if (member.presence(day.date) === ABSENT) HALF_DAY else FULL_DAY)
        teamRepository.saveTeamMember(member)
        notifyMemberDayChange(member, dayIndex)
        return true
    }

    fun onDay(dayIndex: Int): Boolean {
        val day = day(dayIndex)
        day.isHoliday = !day.isHoliday
        if (day.isHoliday) {
            holidayProvider.addHoliday(day.date)
        } else {
            holidayProvider.removeHoliday(day.date)
        }
        notifyDayChange(dayIndex)
        return false
    }

    fun onMemberDayChange(callback: (TeamMember, Int) -> Unit): SprintCalendar {
        this.notifyMemberDayChange = callback
        return this
    }

    fun onMemberChange(callback: (TeamMember) -> Unit): SprintCalendar {
        this.notifyMemberChange = callback
        return this
    }

    fun onDayChange(function: (Int) -> Unit): SprintCalendar {
        notifyDayChange = function
        return this
    }

    fun onFullRefresh(function: () -> Unit): SprintCalendar {
        notifyFullRefresh = function
        return this
    }

    companion object {
        val DAY_PLACEHOLDER = SprintDay(LocalDate(1970, 1, 1), false, false)

    }

    fun deleteTeamMember(teamMember: TeamMember) {
        team.remove(teamMember)
        teamRepository.deleteTeamMember(teamMember)
    }

    fun setAllWeekHolidayFor(member: TeamMember, week: Int) {
        val dates = ((0 + week * 5)..(4 + week * 5)).map { day(it).date }
        var presenceType = if (dates.map { member.presence(it) }.filter { it != ABSENT }.size > 0) ABSENT else FULL_DAY
        for (date in dates) {
            member.setPresence(date, presenceType)
        }
        teamRepository.saveTeamMember(member);
        notifyMemberChange(member)
    }

    fun name(): String {
        val sprintDiffs = firstDate - currentSprintFirstDate
        if (sprintDiffs == 0) {
            return "Current Sprint"
        } else if (sprintDiffs > 0 ) {
            return "Sprint +${sprintDiffs / 14}"
        } else {
            return "Sprint ${sprintDiffs / 14}"
        }
    }

    fun nextSprint() {
        initByStartDate(firstDate + 14)
        notifyFullRefresh()
    }

    fun currentSprint() {
        initByStartDate(currentSprintFirstDate)
        notifyFullRefresh()
    }

    fun previousSprint() {
        initByStartDate(firstDate - 14)
        notifyFullRefresh()
    }

    fun updateDate() {
       if(!cashedToday.equals(dateProvider.today)) {
           initByCurrentDate()
           notifyFullRefresh()
       }
    }
}

operator fun LocalDate.plus(days: Int) = this.plusDays(days)
operator fun LocalDate.minus(days: Int) = this.minusDays(days)
operator fun LocalDate.minus(anotherDate: LocalDate) = Days.daysBetween(anotherDate, this).days
