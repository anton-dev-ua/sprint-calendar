package net.sourcefusion.agiletools.sprintcalendar

import java.util.*

class Team(teamMembers: List<TeamMember>) : Iterable<TeamMember> {
    constructor(vararg teamMembers: TeamMember): this(listOf(*teamMembers))
    private val teamMembers: MutableList<TeamMember>

    init {
        this.teamMembers = ArrayList(teamMembers)
    }

    fun addTeamMember(name: String):TeamMember {
        val teamMember = TeamMember(name)
        teamMembers.add(teamMember)
        return teamMember
    }

    override fun iterator(): Iterator<TeamMember> {
        return object : Iterator<TeamMember> {
            internal var delegate = if(teamMembers.size>0) teamMembers.iterator() else arrayOf(Team.TEAM_MEMBER_PLACEHOLDER).iterator()

            override fun hasNext(): Boolean {
                return delegate.hasNext()
            }

            override fun next(): TeamMember {
                return delegate.next()
            }
        }
    }

    fun member(index: Int): TeamMember {
        if (index < teamMembers.size) {
            return teamMembers[index]
        } else {
            return TEAM_MEMBER_PLACEHOLDER
        }
    }

    val membersCount: Int
        get() = teamMembers.size

    companion object {
        val TEAM_MEMBER_PLACEHOLDER = TeamMember("")
    }
}
