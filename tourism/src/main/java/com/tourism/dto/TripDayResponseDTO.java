package com.tourism.dto;

import java.util.List;

public class TripDayResponseDTO {

    private int dayNumber;
    private List<ScheduleItemDTO> schedules;

    // getters & setters

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public List<ScheduleItemDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleItemDTO> schedules) {
        this.schedules = schedules;
    }

}
