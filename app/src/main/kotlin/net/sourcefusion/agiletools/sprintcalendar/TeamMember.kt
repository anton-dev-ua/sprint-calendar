package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

data public class TeamMember(val name: String) {

    private val absenceMap = hashMapOf<LocalDate, PresenceType>()

    fun setPresence(localDate: LocalDate, presenceType: PresenceType) {
        absenceMap[localDate] = presenceType
    }

    fun presence(date: LocalDate): PresenceType {
        val presenceType = absenceMap[date]
        return PresenceType.safePresence(presenceType)
    }
}
