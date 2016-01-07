package com.example.anton.sprintcalendar;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class TeamMember {
    private String name;
    private Map<LocalDate, AbsenceType> absenceMap = new HashMap<>();

    public TeamMember(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void addAbsence(LocalDate localDate, AbsenceType absenceType) {
        absenceMap.put(localDate, absenceType);
    }

    public int presence(LocalDate date) {
        AbsenceType absenceType = absenceMap.get(date);
        return AbsenceType.getPresencePercentage(absenceType);
    }
}
