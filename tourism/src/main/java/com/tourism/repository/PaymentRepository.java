package com.tourism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tourism.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
