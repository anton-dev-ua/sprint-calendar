package net.sourcefusion.agiletools.sprintcalendar

import java.util.Arrays

public class Team(vararg teamMembers: TeamMember) : Iterable<TeamMember> {
    private val teamMembers: List<TeamMember>

    init {
        this.teamMembers = Arrays.asList(*teamMembers)
    }


    override fun iterator(): Iterator<TeamMember> {
        return object : Iterator<TeamMember> {
            internal var delegate = teamMembers.iterator()

            override fun hasNext(): Boolean {
                return delegate.hasNext()
            }

            override fun next(): TeamMember {
                return delegate.next()
            }

//            override fun remove() {
//
//            }
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
