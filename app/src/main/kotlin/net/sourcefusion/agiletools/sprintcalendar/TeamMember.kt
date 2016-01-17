package net.sourcefusion.agiletools.sprintcalendar

import com.google.common.base.MoreObjects

import org.joda.time.LocalDate

import java.util.HashMap

class TeamMember(val name: String) {
    private val absenceMap = HashMap<LocalDate, PresenceType>()

    fun setPresence(localDate: LocalDate, presenceType: PresenceType) {
        absenceMap.put(localDate, presenceType)
    }

    fun presence(date: LocalDate): PresenceType {
        val presenceType = absenceMap[date]
        return PresenceType.safePresence(presenceType)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this).add("name", name).toString()
    }
}
