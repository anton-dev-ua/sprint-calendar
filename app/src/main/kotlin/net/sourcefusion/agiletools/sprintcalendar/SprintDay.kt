package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

public data class SprintDay(val date: LocalDate, var isHoliday: Boolean, val isToday: Boolean)
