package com.tourism.dto;

import java.util.List;

public class TripResponseDTO {

    private Long tripId;
    private String budget;
    private String travelType;
    private String pace;
    private String startTime;
    private List<TripDayResponseDTO> days;

    // getters & setters

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<TripDayResponseDTO> getDays() {
        return days;
    }

    public void setDays(List<TripDayResponseDTO> days) {
        this.days = days;
    }
    
}
