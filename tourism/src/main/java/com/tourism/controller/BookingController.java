package com.tourism.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.dto.BookingRequestDTO;
import com.tourism.dto.BookingResponseDTO;
import com.tourism.entity.Booking;
import com.tourism.security.JwtUtil;
import com.tourism.services.BookingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    public BookingController(
            BookingService bookingService,
            JwtUtil jwtUtil
    ) {
        this.bookingService = bookingService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public BookingResponseDTO createBooking(
            @RequestBody BookingRequestDTO request,
            HttpServletRequest httpRequest
    ) {
        String userEmail = jwtUtil.extractEmailFromRequest(httpRequest);
        Booking booking = bookingService.createBooking(request, userEmail);
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setTotalAmount(booking.getTotalAmount());
        return dto;
    }
}
