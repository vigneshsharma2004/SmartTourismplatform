package com.tourism.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tourism.dto.ItineraryDayDTO;
import com.tourism.dto.ItineraryRequestDTO;
import com.tourism.dto.PreferencesDTO;
import com.tourism.dto.ScheduleItemDTO;
import com.tourism.entity.Place;
import com.tourism.repository.PlaceRepository;

@Service
public class ItineraryService {

    private final PlaceRepository placeRepository;

    public ItineraryService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<ItineraryDayDTO> generateItinerary(ItineraryRequestDTO request) {

        if (request == null || request.getPreferences() == null) {
            throw new IllegalArgumentException("Preferences must not be null");
        }

        PreferencesDTO prefs = request.getPreferences();
        if (prefs.getStartTime() == null || prefs.getStartTime().isBlank()) {
            prefs.setStartTime("09:00");
        }

        List<Integer> placeIds = request.getSelectedPlaces() == null ? List.of() : request.getSelectedPlaces().stream()
                .map(p -> p.getPlaceId())
                .filter(id -> id != null)
                .collect(Collectors.toList());
        if (placeIds.isEmpty()) {
            throw new IllegalArgumentException("No places selected");
        }

        // Fetch places from DB
        List<Place> places = placeRepository.findAllById(placeIds);

        int placesPerDay = 2;
        if ("MODERATE".equals(prefs.getPace())) placesPerDay = 3;
        if ("FAST".equals(prefs.getPace())) placesPerDay = 4;

        List<ItineraryDayDTO> result = new ArrayList<>();
        int index = 0;
        int day = 1;

        while (index < places.size()) {

            LocalTime currentTime = LocalTime.parse(prefs.getStartTime());

            List<ScheduleItemDTO> schedule = new ArrayList<>();

            for (int i = 0; i < placesPerDay && index < places.size(); i++) {

                // Lunch break
                if (!currentTime.isBefore(LocalTime.of(13, 0)) &&
                    currentTime.isBefore(LocalTime.of(14, 0))) {

                    schedule.add(new ScheduleItemDTO(
                        "13:00 – 14:00",
                        "Lunch Break 🍽"
                    ));
                    currentTime = LocalTime.of(14, 0);
                }

                // Place visit (2 hours)
                Place place = places.get(index);
                LocalTime end = currentTime.plusHours(2);

                schedule.add(new ScheduleItemDTO(
                    currentTime + " – " + end,
                    place.getPlaceName()
                ));

                currentTime = end;
                index++;

                // Travel buffer
                if (index < places.size()) {
                    LocalTime travelEnd = currentTime.plusMinutes(30);
                    schedule.add(new ScheduleItemDTO(
                        currentTime + " – " + travelEnd,
                        "Travel / Buffer 🚗"
                    ));
                    currentTime = travelEnd;
                }
            }

            result.add(new ItineraryDayDTO(day++, schedule));
        }

        return result;
    }
}
