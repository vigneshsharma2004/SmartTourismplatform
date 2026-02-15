package com.tourism.dto;

import java.util.List;

public class ItineraryDayDTO {

    private int day;
    private List<ScheduleItemDTO> schedule;

    public ItineraryDayDTO(int day, List<ScheduleItemDTO> schedule) {
        this.day = day;
        this.schedule = schedule;
    }

    public int getDay() {
        return day;
    }

    public List<ScheduleItemDTO> getSchedule() {
        return schedule;
    }
}
