package com.example.anton.sprintcalendar;

public enum AbsenceType {
    FULL_DAY(0), HALF_DAY(50);

    private int presencePercentage;

    AbsenceType(int presencePercentage) {
        this.presencePercentage = presencePercentage;
    }

    public float presence() {
        return presencePercentage;
    }

    public static int getPresencePercentage(AbsenceType absenceMap) {
        return absenceMap == null ? 100 : absenceMap.presencePercentage;
    }
}
