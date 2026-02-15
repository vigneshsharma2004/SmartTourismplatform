package com.tourism.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.dto.ItineraryDayDTO;
import com.tourism.dto.ItineraryRequestDTO;
import com.tourism.dto.TripResponseDTO;
import com.tourism.entity.Trip;
import com.tourism.security.JwtUtil;
import com.tourism.services.ItineraryService;
import com.tourism.services.TripService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")
public class TripController {

    private final ItineraryService itineraryService;
    private final TripService tripService;
    private final JwtUtil jwtUtil;

    public TripController(ItineraryService itineraryService,
                          TripService tripService,
                          JwtUtil jwtUtil) {
        this.itineraryService = itineraryService;
        this.tripService = tripService;
        this.jwtUtil = jwtUtil;
    }

    /* ================= GENERATE & SAVE TRIP ================= */

    @PostMapping("/generate-and-save")
    public Long generateAndSaveTrip(
            @RequestBody ItineraryRequestDTO request,
            HttpServletRequest httpRequest
    ) {

        String userEmail = jwtUtil.extractEmailFromRequest(httpRequest);

        List<ItineraryDayDTO> itinerary =
                itineraryService.generateItinerary(request);

        Trip trip = tripService.saveTrip(
                request,
                itinerary,
                userEmail
        );

        return trip.getTripId();
    }

    /* ================= GET SAVED TRIP BY ID ================= */

    @GetMapping("/{tripId}")
    public TripResponseDTO getTripById(@PathVariable Long tripId) {
        return tripService.getTripResponse(tripId);
    }

    /* ================= GET LOGGED-IN USER TRIPS ================= */

    @GetMapping("/my")
    public List<TripResponseDTO> getMyTrips(HttpServletRequest request) {

        String userEmail = jwtUtil.extractEmailFromRequest(request);

        return tripService.getTripsForUser(userEmail);
    }
    @DeleteMapping("/{tripId}")
    public void deleteTrip(
             @PathVariable Long tripId,
        HttpServletRequest request
    )   {
        String userEmail = jwtUtil.extractEmailFromRequest(request);
            tripService.deleteTrip(tripId, userEmail);
        }

}
