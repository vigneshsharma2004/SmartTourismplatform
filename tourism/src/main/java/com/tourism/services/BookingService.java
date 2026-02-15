package com.tourism.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tourism.dto.BookingRequestDTO;
import com.tourism.entity.Booking;
import com.tourism.entity.Place;
import com.tourism.entity.Trip;
import com.tourism.entity.User;
import com.tourism.repository.BookingRepository;
import com.tourism.repository.PlaceRepository;
import com.tourism.repository.TripRepository;
import com.tourism.repository.UserRepository;
import com.tourism.repository.HotelRoomRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final HotelRoomRepository hotelRoomRepository;

    public BookingService(
            BookingRepository bookingRepository,
            PlaceRepository placeRepository,
            UserRepository userRepository,
            TripRepository tripRepository,
            HotelRoomRepository hotelRoomRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.hotelRoomRepository = hotelRoomRepository;
    }

    @Transactional
    public Booking createBooking(BookingRequestDTO request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Double hotelAmount = calculateHotelAmount(
                request.getHotelRoomId(),
                request.getNights()
        );
        Double asiAmount = calculateAsiTicketAmount(request.getSelectedPlaceIds());
        Double totalAmount = hotelAmount + asiAmount;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setHotelAmount(hotelAmount);
        booking.setAsiTicketAmount(asiAmount);
        booking.setTotalAmount(totalAmount);

        return bookingRepository.save(booking);
    }

    private Double calculateHotelAmount(Long hotelRoomId, int nights) {
        if (hotelRoomId == null || nights <= 0) {
            return 0.0;
        }
        return hotelRoomRepository.findById(hotelRoomId)
                .map(room -> room.getPricePerNight() * nights)
                .orElseThrow(() -> new RuntimeException("Hotel room not found"));
    }

    /* ===== ASI COST CALCULATION ===== */
    private Double calculateAsiTicketAmount(List<Long> placeIds) {

        if (placeIds == null || placeIds.isEmpty()) {
            return 0.0;
        }

        List<Integer> placeIdsAsInt = placeIds.stream().map(Long::intValue).collect(Collectors.toList());
        List<Place> places = placeRepository.findAllById(placeIdsAsInt);

        return places.stream()
        .filter(place -> place.isAsiMonument())
        .map(place -> place.getEntryFee().doubleValue())
        .reduce(0.0, Double::sum);

    }
}
