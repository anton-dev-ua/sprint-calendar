package com.example.anton.sprintcalendar;

public enum PresenceType {
    NONE(0), HALF_DAY(2.5f), FULL_DAY(5);

    private float presencePercentage;

    PresenceType(float presencePercentage) {
        this.presencePercentage = presencePercentage;
    }

    public float hours() {
        return presencePercentage;
    }

    public static PresenceType safePresence(PresenceType absence) {
        return absence == null ? FULL_DAY : absence;
    }
}
