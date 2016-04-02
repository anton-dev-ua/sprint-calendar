package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

data class TeamMember(val name: String, var id:Long? = null) {

    private val absenceMap = hashMapOf<LocalDate, PresenceType>()

    fun setPresence(localDate: LocalDate, presenceType: PresenceType) {
        absenceMap[localDate] = presenceType
    }

    fun presence(date: LocalDate): PresenceType {
        val presenceType = absenceMap[date]
        return PresenceType.safePresence(presenceType)
    }

    fun presenceIterator() = absenceMap.iterator()
}
