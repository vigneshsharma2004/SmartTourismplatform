 package com.tourism.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trip_day")
public class TripDay {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long dayId;

    private int dayNumber;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonIgnore
    private Trip trip;

    
   @OneToMany(
    mappedBy = "day",
    cascade = CascadeType.ALL,
    orphanRemoval = true
    )
    private List<TripSchedule> schedules;

    // getters & setters

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<TripSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<TripSchedule> schedules) {
        this.schedules = schedules;
    }
}
