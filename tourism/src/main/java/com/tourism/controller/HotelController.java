package com.tourism.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.entity.Hotel;
import com.tourism.entity.HotelRoom;
import com.tourism.repository.HotelRepository;
import com.tourism.repository.HotelRoomRepository;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "http://localhost:5173")
public class HotelController {

    private final HotelRepository hotelRepository;
    private final HotelRoomRepository roomRepository;

    public HotelController(
        HotelRepository hotelRepository,
        HotelRoomRepository roomRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/city/{cityName}")
    public List<Hotel> getHotelsByCity(@PathVariable String cityName) {
        return hotelRepository.findByCityName(cityName);
    }

    @GetMapping("/{hotelId}/rooms")
    public List<HotelRoom> getRooms(@PathVariable Long hotelId) {
        return roomRepository.findByHotelHotelId(hotelId);
    }
}

