package com.example.anton.sprintcalendar;

import java.util.Arrays;
import java.util.List;

public class Team {
    public static final TeamMember TEAM_MEMBER_PLACEHOLDER = new TeamMember("");
    private List<TeamMember> teamMembers;

    public Team(TeamMember... teamMembers) {
        this.teamMembers = Arrays.asList(teamMembers);
    }


    public List<TeamMember> getTeamMember() {
        return teamMembers;
    }

    public TeamMember member(int index) {
        if (index < teamMembers.size()) {
            return teamMembers.get(index);
        } else {
            return TEAM_MEMBER_PLACEHOLDER;
        }
    }
}
