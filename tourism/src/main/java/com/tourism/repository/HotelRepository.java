package com.tourism.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCityName(String cityName);
}
