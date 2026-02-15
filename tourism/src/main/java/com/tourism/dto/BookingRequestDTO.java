package com.tourism.dto;

import java.util.List;

public class BookingRequestDTO {

    private Long tripId;
    private Long hotelRoomId;
    private int nights;
    private List<Long> selectedPlaceIds; // for ASI tickets

    // getters & setters

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getHotelRoomId() {
        return hotelRoomId;
    }

    public void setHotelRoomId(Long hotelRoomId) {
        this.hotelRoomId = hotelRoomId;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public List<Long> getSelectedPlaceIds() {
        return selectedPlaceIds;
    }

    public void setSelectedPlaceIds(List<Long> selectedPlaceIds) {
        this.selectedPlaceIds = selectedPlaceIds;
    }
    
}
