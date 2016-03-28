package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import net.sourcefusion.agiletools.sprintcalendar.Team
import net.sourcefusion.agiletools.sprintcalendar.TeamMember
import net.sourcefusion.agiletools.sprintcalendar.persisting.TeamRepository

class SugarTeamRepository: TeamRepository {
    override fun readTeam(): Team {
        val allTeamMembers = TeamMemberEntry.all()
        return Team(allTeamMembers.map { TeamMember(it.name, it.id) })
    }

    override fun saveTeamMember(teamMember: TeamMember) {
        val id = TeamMemberEntry(teamMember.name).save();
        teamMember.id = id;
    }
}