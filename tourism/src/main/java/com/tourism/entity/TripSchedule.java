package com.tourism.entity ;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trip_schedule")
public class TripSchedule {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long scheduleId;

    private String time;
    private String label;

    @ManyToOne
    @JoinColumn(name = "day_id")
    @JsonIgnore
    private TripDay day;

    // getters & setters

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TripDay getDay() {
        return day;
    }

    public void setDay(TripDay day) {
        this.day = day;
    }

}
