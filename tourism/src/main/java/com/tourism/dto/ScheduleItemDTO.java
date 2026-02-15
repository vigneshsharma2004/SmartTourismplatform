package com.tourism.dto;

public class ScheduleItemDTO {

    private String time;
    private String label;

    public ScheduleItemDTO() {
    }

    public ScheduleItemDTO(String time, String label) {
        this.time = time;
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public String getLabel() {
        return label;
    }
}

