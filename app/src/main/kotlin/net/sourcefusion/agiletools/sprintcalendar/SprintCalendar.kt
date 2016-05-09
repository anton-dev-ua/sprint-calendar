package net.sourcefusion.agiletools.sprintcalendar

import net.sourcefusion.agiletools.sprintcalendar.PresenceType.*
import net.sourcefusion.agiletools.sprintcalendar.persisting.TeamRepository
import org.joda.time.Days
import org.joda.time.LocalDate
import java.lang.Math.max
import java.lang.Math.min
import kotlin.properties.Delegates

class SprintCalendar(val teamRepository: TeamRepository, private val dateProvider: DateProvider, val holidayProvider: HolidayProvider) {

    private val sprintBaseDate = LocalDate(2015, 12, 7)

    private val namingStrategy = SequenceInYearNaming()

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
    private var cashedHour by Delegates.notNull<Int>()

    var currentSprintFirstDate by Delegates.notNull<LocalDate>()
        private set

    init {
        initByCurrentDate()
    }


    fun initByCurrentDate() {
        val today = dateProvider.today
        cashedToday = today
        cashedHour = dateProvider.time.hourOfDay
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
        get() = hoursBetween(dateProvider.today, true).toInt()

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

    private fun hoursBetween(startDate: LocalDate, calculationOfLeft: Boolean = false): Float {
        var totalHours = 0f
        for (sprintDay in days) {
            if (!sprintDay.isHoliday && sprintDay.date.compareTo(startDate) >= 0) {
                for (member in team) {
                    if (member == Team.TEAM_MEMBER_PLACEHOLDER) continue
                    val presenceType = member.presence(sprintDay.date)
                    val effectiveHours = presenceType.hours
                    totalHours += effectiveHours
                    if (calculationOfLeft && sprintDay.date.equals(dateProvider.today)) {
                        val curTime = dateProvider.time
                        val presenceDayHours = presenceDayHours(presenceType)
                        val pastDayHours = when (presenceType) {
                            HALF_DAY -> max(min(curTime.hourOfDay - 13, presenceDayHours), 0)
                            else -> max(min(curTime.hourOfDay - 9, presenceDayHours), 0)
                        }

                        println("curTime.hourOfDay: ${curTime.hourOfDay}")
                        println("effectiveHours: $effectiveHours, pastDayHours: $pastDayHours, presenceDayHours: $presenceDayHours")

                        totalHours -= effectiveHours * pastDayHours / presenceDayHours
                    }
                }
            }
        }
        return totalHours
    }

    private fun presenceDayHours(presence: PresenceType): Int {
        return when (presence) {
            HALF_DAY_MORNING, HALF_DAY -> 4
            else -> 8
        }
    }

    fun addTeamMember(name: String) {
        val teamMember = team.addTeamMember(name)
        teamRepository.saveTeamMember(teamMember)
    }

    fun toggleFullDayMember(member: TeamMember, dayIndex: Int) =
            updateMemberPresence(member, dayIndex, if (member.presence(day(dayIndex).date) === FULL_DAY) ABSENT else FULL_DAY)

    fun toggleHalfDayMember(member: TeamMember, dayIndex: Int) =
            updateMemberPresence(member, dayIndex, if (member.presence(day(dayIndex).date) === HALF_DAY) HALF_DAY_MORNING else HALF_DAY)

    fun fullDayAbsent(member: TeamMember, dayIndex: Int) =
            updateMemberPresence(member, dayIndex, if (member.presence(day(dayIndex).date) == ABSENT) BUSINESS_TRIP else ABSENT)

    fun fullDayPresent(member: TeamMember, dayIndex: Int) = updateMemberPresence(member, dayIndex, FULL_DAY)

    private fun updateMemberPresence(member: TeamMember, dayIndex: Int, presence: PresenceType) {
        member.setPresence(day(dayIndex).date, presence)
        teamRepository.saveTeamMember(member)
        notifyMemberDayChange(member, dayIndex)
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

    fun toggleAllWeekHolidayFor(member: TeamMember, week: Int) {
        val dates = ((0 + week * 5)..(4 + week * 5)).map { day(it).date }
        var presenceType = if (dates.map { member.presence(it) }.filter { it != ABSENT }.size > 0) ABSENT else FULL_DAY
        updateMemberWeek(member, week, presenceType)
    }

    fun wholeWeekAbsent(member: TeamMember, week: Int) {
        val dates = ((0 + week * 5)..(4 + week * 5)).map { day(it).date }
        var presenceType = if (dates.map { member.presence(it) }.filter { it == ABSENT }.size == 5) BUSINESS_TRIP else ABSENT
        updateMemberWeek(member, week, presenceType)
    }

    fun wholeWeekPresent(member: TeamMember, week: Int) {
        updateMemberWeek(member, week, FULL_DAY)
    }

    private fun updateMemberWeek(member: TeamMember, week: Int, presenceType: PresenceType) {
        val dates = ((0 + week * 5)..(4 + week * 5)).map { day(it).date }
        for (date in dates) {
            member.setPresence(date, presenceType)
        }
        teamRepository.saveTeamMember(member);
        notifyMemberChange(member)
    }

    fun name(): String {
        return namingStrategy.nameFor(this)
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
        if (cashedToday != (dateProvider.today) || cashedHour != dateProvider.time.hourOfDay) {
            initByCurrentDate()
            notifyFullRefresh()
        }
    }

    fun sprintShift(): Int = (firstDate - currentSprintFirstDate) / 14
}

operator fun LocalDate.plus(days: Int) = this.plusDays(days)
operator fun LocalDate.minus(days: Int) = this.minusDays(days)
operator fun LocalDate.minus(anotherDate: LocalDate) = Days.daysBetween(anotherDate, this).days
