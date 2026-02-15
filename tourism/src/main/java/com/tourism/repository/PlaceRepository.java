package com.tourism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
