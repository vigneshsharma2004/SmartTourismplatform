package com.tourism.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourism.dto.ItineraryDayDTO;
import com.tourism.dto.ItineraryRequestDTO;
import com.tourism.dto.ScheduleItemDTO;
import com.tourism.dto.TripDayResponseDTO;
import com.tourism.dto.TripResponseDTO;
import com.tourism.entity.Trip;
import com.tourism.entity.TripDay;
import com.tourism.entity.TripSchedule;
import com.tourism.entity.User;
import com.tourism.repository.TripRepository;
import com.tourism.repository.UserRepository;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(TripRepository tripRepository,
                       UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    /* ================= SAVE TRIP (JWT USER) ================= */

    @Transactional
    public Trip saveTrip(
            ItineraryRequestDTO request,
            List<ItineraryDayDTO> itinerary,
            String userEmail
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setBudget(request.getPreferences().getBudget());
        trip.setTravelType(request.getPreferences().getTravelType());
        trip.setPace(request.getPreferences().getPace());
        trip.setStartTime(request.getPreferences().getStartTime());

        List<TripDay> days = new ArrayList<>();

        for (ItineraryDayDTO d : itinerary) {
            TripDay day = new TripDay();
            day.setDayNumber(d.getDay());
            day.setTrip(trip);

            List<TripSchedule> schedules = new ArrayList<>();
            for (ScheduleItemDTO s : d.getSchedule()) {
                TripSchedule schedule = new TripSchedule();
                schedule.setLabel(s.getLabel());
                schedule.setTime(s.getTime());
                schedule.setDay(day);
                schedules.add(schedule);
            }

            day.setSchedules(schedules);
            days.add(day);
        }

        trip.setDays(days);

        return tripRepository.save(trip);
    }

    /* ================= GET TRIP RESPONSE ================= */

    @Transactional(readOnly = true)
    public TripResponseDTO getTripResponse(Long tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        return mapToResponseDTO(trip);
    }

    /* ================= GET USER TRIPS ================= */

    @Transactional(readOnly = true)
    public List<TripResponseDTO> getTripsForUser(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<TripResponseDTO> result = new ArrayList<>();

        for (Trip trip : user.getTrips()) {
            result.add(mapToResponseDTO(trip));
        }

        return result;
    }

    /* ================= ENTITY → DTO MAPPER ================= */

    private TripResponseDTO mapToResponseDTO(Trip trip) {

        TripResponseDTO dto = new TripResponseDTO();
        dto.setTripId(trip.getTripId());
        dto.setBudget(trip.getBudget());
        dto.setTravelType(trip.getTravelType());
        dto.setPace(trip.getPace());
        dto.setStartTime(trip.getStartTime());

        List<TripDayResponseDTO> dayDTOs = new ArrayList<>();

        for (TripDay day : trip.getDays()) {
            TripDayResponseDTO dayDTO = new TripDayResponseDTO();
            dayDTO.setDayNumber(day.getDayNumber());

            List<ScheduleItemDTO> schedules = new ArrayList<>();
            for (TripSchedule s : day.getSchedules()) {
                schedules.add(
                        new ScheduleItemDTO(
                                s.getTime(),
                                s.getLabel()
                        )
                );
            }

            dayDTO.setSchedules(schedules);
            dayDTOs.add(dayDTO);
        }

        dto.setDays(dayDTOs);
        return dto;
    }
    @Transactional
    public void deleteTrip(Long tripId, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Trip trip = tripRepository
            .findByTripIdAndUserUserId(tripId, user.getUserId())
            .orElseThrow(() -> new RuntimeException("Trip not found or access denied"));

    tripRepository.delete(trip);
}

}
