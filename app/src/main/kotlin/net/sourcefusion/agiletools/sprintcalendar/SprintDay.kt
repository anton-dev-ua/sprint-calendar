package net.sourcefusion.agiletools.sprintcalendar

import org.joda.time.LocalDate

data class SprintDay(val date: LocalDate, val index: Int, var isHoliday: Boolean, val isToday: Boolean)
