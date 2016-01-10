package com.example.anton.sprintcalendar;

import com.google.common.base.MoreObjects;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class TeamMember {
    private String name;
    private Map<LocalDate, PresenceType> absenceMap = new HashMap<>();

    public TeamMember(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setPresence(LocalDate localDate, PresenceType presenceType) {
        absenceMap.put(localDate, presenceType);
    }

    public PresenceType presence(LocalDate date) {
        PresenceType presenceType = absenceMap.get(date);
        return PresenceType.safePresence(presenceType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
