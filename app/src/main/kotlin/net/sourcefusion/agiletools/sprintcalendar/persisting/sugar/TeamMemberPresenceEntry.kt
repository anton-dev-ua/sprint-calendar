package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import com.orm.SugarRecord
import com.orm.dsl.MultiUnique
import java.util.*

@MultiUnique("teamMember, date")
class TeamMemberPresenceEntry(var teamMember: TeamMemberEntry?, var date: Date?, var presenceType: String?): SugarRecord() {
    constructor(): this(null, null, null)
}