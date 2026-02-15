package com.tourism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.TripDay;

public interface TripDayRepository extends JpaRepository<TripDay, Long> {

}
