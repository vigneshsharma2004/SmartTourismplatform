package com.tourism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.TripSchedule;

public interface TripScheduleRepository  extends JpaRepository<TripSchedule, Long>{

}
