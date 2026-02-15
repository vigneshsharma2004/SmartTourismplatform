package com.tourism.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.HotelRoom;

public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long> {
    List<HotelRoom> findByHotelHotelId(Long hotelId);
}
