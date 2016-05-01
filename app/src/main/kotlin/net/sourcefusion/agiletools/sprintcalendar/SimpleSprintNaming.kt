package net.sourcefusion.agiletools.sprintcalendar

class SimpleSprintNaming : SprintNamingStrategy {

    override fun nameFor(sprintCalendar: SprintCalendar): String {
        val sprintDiffs = sprintCalendar.firstDate - sprintCalendar.currentSprintFirstDate
        if (sprintDiffs == 0) {
            return "Current Sprint"
        } else if (sprintDiffs > 0 ) {
            return "Sprint +${sprintDiffs / 14}"
        } else {
            return "Sprint ${sprintDiffs / 14}"
        }
    }
}