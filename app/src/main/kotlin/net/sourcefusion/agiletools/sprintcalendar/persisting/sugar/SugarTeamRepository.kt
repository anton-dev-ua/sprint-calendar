package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import com.orm.SugarRecord
import net.sourcefusion.agiletools.sprintcalendar.PresenceType
import net.sourcefusion.agiletools.sprintcalendar.Team
import net.sourcefusion.agiletools.sprintcalendar.TeamMember
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils.toDate
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils.toLocalDate
import net.sourcefusion.agiletools.sprintcalendar.persisting.TeamRepository

class SugarTeamRepository : TeamRepository {

    override fun readTeam(): Team {
        val allTeamMembers = TeamMemberEntry.all()
        return Team(allTeamMembers.map {
            val teamMember = TeamMember(it.name, it.id)
            for (presence in it.getPresence()) {
                teamMember.setPresence(toLocalDate(presence.date), PresenceType.valueOf(presence?.presenceType ?: PresenceType.FULL_DAY.name))
            }
            teamMember
        })
    }

    override fun saveTeamMember(teamMember: TeamMember) {
        val teamMemberEntry = TeamMemberEntry(teamMember.name, teamMember.id)
        val id = teamMemberEntry.save();
        teamMember.id = id;
        for (presence in teamMember.presenceIterator()) {
            if (presence.value == PresenceType.FULL_DAY) {
                SugarRecord.deleteAll(TeamMemberPresenceEntry::class.java, "team_member = ? and date = ?", "${teamMember.id}", "${toDate(presence.key).time}")
            } else {
                TeamMemberPresenceEntry(teamMemberEntry, toDate(presence.key), presence.value.name).save()
            }
        }
    }

    override fun deleteTeamMember(teamMember: TeamMember) {
        SugarRecord.deleteAll(TeamMemberPresenceEntry::class.java, "team_member = ?", "${teamMember.id}")
        TeamMemberEntry.findById(teamMember.id).delete()
    }
}
