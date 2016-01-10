package com.example.anton.sprintcalendar;

public enum PresenceType {
    NONE(0), HALF_DAY(3), FULL_DAY(5);

    private int presencePercentage;

    PresenceType(int presencePercentage) {
        this.presencePercentage = presencePercentage;
    }

    public float hours() {
        return presencePercentage;
    }

    public static PresenceType safePresence(PresenceType absence) {
        return absence == null ? FULL_DAY : absence;
    }
}
