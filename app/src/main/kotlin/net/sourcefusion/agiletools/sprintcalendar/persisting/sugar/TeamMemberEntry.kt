package net.sourcefusion.agiletools.sprintcalendar.persisting.sugar

import com.orm.SugarRecord

class TeamMemberEntry(var name: String) : SugarRecord() {
    constructor() : this("")

    companion object {
        fun all() = listAll(TeamMemberEntry::class.java)
        fun findById(id: Long?) = findById(TeamMemberEntry::class.java, id)
    }
}