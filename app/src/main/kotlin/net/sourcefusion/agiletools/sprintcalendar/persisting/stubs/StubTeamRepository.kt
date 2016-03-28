package net.sourcefusion.agiletools.sprintcalendar.persisting.stubs

import net.sourcefusion.agiletools.sprintcalendar.Team
import net.sourcefusion.agiletools.sprintcalendar.TeamMember
import net.sourcefusion.agiletools.sprintcalendar.persisting.TeamRepository

class StubTeamRepository(val team: Team) : TeamRepository {

    override fun readTeam() = team

    override fun saveTeamMember(teamMember: TeamMember) {
    }

    override fun deleteTeamMember(teamMember: TeamMember) {
    }

}
