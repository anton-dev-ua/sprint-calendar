package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import com.orm.SugarRecord

class TeamMemberEntry(var name: String, id: Long?) : SugarRecord() {
    constructor() : this("", null)

    init {
       this.id = id
    }

    fun getPresence() = SugarRecord.find(TeamMemberPresenceEntry::class.java, "team_member = ?", "${this.id}")

    companion object {
        fun all() = listAll(TeamMemberEntry::class.java)
        fun findById(id: Long?) = findById(TeamMemberEntry::class.java, id)
    }
}