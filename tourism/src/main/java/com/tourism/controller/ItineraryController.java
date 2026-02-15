package com.tourism.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.dto.ItineraryDayDTO;
import com.tourism.dto.ItineraryRequestDTO;
import com.tourism.services.ItineraryService;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/generate")
    public List<ItineraryDayDTO> generate(
            @RequestBody ItineraryRequestDTO request) {
        return itineraryService.generateItinerary(request);
    }
}
