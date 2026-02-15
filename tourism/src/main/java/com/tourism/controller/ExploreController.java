package com.tourism.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.dto.CityDTO;
import com.tourism.dto.PlaceDTO;
import com.tourism.dto.StateDTO;
import com.tourism.services.ExploreService;

@RestController
@RequestMapping
public class ExploreController {

    private final ExploreService exploreService;

    public ExploreController(ExploreService exploreService) {
        this.exploreService = exploreService;
    }

    @GetMapping("/states")
    public List<StateDTO> getStates() {
        return exploreService.getStates();
    }

    @GetMapping("/cities")
    public List<CityDTO> getCities(@RequestParam Integer stateId) {
        return exploreService.getCities(stateId);
    }

    @GetMapping("/places")
    public List<PlaceDTO> getPlaces(@RequestParam Integer cityId) {
        return exploreService.getPlaces(cityId);
    }
}
