package net.sourcefusion.agiletools.sprintcalendar

interface SprintNamingStrategy {
    fun nameFor(sprintCalendar: SprintCalendar): String
}