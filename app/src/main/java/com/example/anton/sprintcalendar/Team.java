package com.example.anton.sprintcalendar;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Team implements Iterable<TeamMember> {
    public static final TeamMember TEAM_MEMBER_PLACEHOLDER = new TeamMember("");
    private List<TeamMember> teamMembers;

    public Team(TeamMember... teamMembers) {
        this.teamMembers = Arrays.asList(teamMembers);
    }


    public Iterator<TeamMember> iterator() {
        return new Iterator<TeamMember>() {
            Iterator<TeamMember> delegate = teamMembers.iterator();

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public TeamMember next() {
                return delegate.next();
            }

            @Override
            public void remove() {

            }
        };
    }

    public TeamMember member(int index) {
        if (index < teamMembers.size()) {
            return teamMembers.get(index);
        } else {
            return TEAM_MEMBER_PLACEHOLDER;
        }
    }

    public int getMembersCount() {
        return teamMembers.size();
    }
}
