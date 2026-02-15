package com.tourism.dto;

public class PreferencesDTO {

    private String budget;        // LOW / MEDIUM / HIGH
    private String travelType;    // SOLO / FAMILY / FRIENDS
    private String pace;          // RELAXED / MODERATE / FAST
    private String startTime;     // e.g. "08:00"

    public PreferencesDTO() {
    }

    /* ===== GETTERS ===== */

    public String getBudget() {
        return budget;
    }

    public String getTravelType() {
        return travelType;
    }

    public String getPace() {
        return pace;
    }

    public String getStartTime() {
        return startTime;
    }

    /* ===== SETTERS ===== */

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
