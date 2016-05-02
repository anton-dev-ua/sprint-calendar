package net.sourcefusion.agiletools.sprintcalendar

enum class PresenceType(val hours: Float) {
    ABSENT(0f), HALF_DAY(2.5f), HALF_DAY_MORNING(2.5f), FULL_DAY(5f);

    companion object {
        fun safePresence(absence: PresenceType?): PresenceType {
            return absence ?: FULL_DAY
        }
    }
}
