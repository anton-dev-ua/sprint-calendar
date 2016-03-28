package net.sourcefusion.agiletools.sprintcalendar.persisting

import net.sourcefusion.agiletools.sprintcalendar.Team
import net.sourcefusion.agiletools.sprintcalendar.TeamMember

interface TeamRepository {
    fun readTeam() : Team
    fun saveTeamMember(teamMember: TeamMember)
}