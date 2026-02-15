package com.tourism.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Trip trip;

    private Double hotelAmount;
    private Double asiTicketAmount;
    private Double totalAmount;

    private String status; // PENDING, CONFIRMED, CANCELLED

    private LocalDateTime createdAt;

    @PrePersist
    void created() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // getters & setters

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Double getHotelAmount() {
        return hotelAmount;
    }

    public void setHotelAmount(Double hotelAmount) {
        this.hotelAmount = hotelAmount;
    }

    public Double getAsiTicketAmount() {
        return asiTicketAmount;
    }

    public void setAsiTicketAmount(Double asiTicketAmount) {
        this.asiTicketAmount = asiTicketAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
