package com.tourism.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {

    // 🔐 Ownership check (used for delete)
    Optional<Trip> findByTripIdAndUserUserId(Long tripId, Long userId);

    // 👤 Fetch trips for logged-in user (optional alternative)
    List<Trip> findByUserUserId(Long userId);
}
