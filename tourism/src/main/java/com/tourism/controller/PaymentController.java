package com.tourism.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourism.dto.PaymentOrderDTO;
import com.tourism.entity.Payment;
import com.tourism.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order/{bookingId}")
    public PaymentOrderDTO createOrder(@PathVariable Long bookingId) throws Exception {
        Payment payment = paymentService.createRazorpayOrder(bookingId);
        PaymentOrderDTO dto = new PaymentOrderDTO();
        dto.setRazorpayOrderId(payment.getRazorpayOrderId());
        dto.setAmount(payment.getAmount());
        return dto;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleRazorpayNotConfigured(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", ex.getMessage()));
    }
    @PostMapping("/verify")
public void verifyPayment(@RequestBody Map<String, String> payload)
        throws Exception {

    paymentService.verifyPayment(
            payload.get("razorpay_order_id"),
            payload.get("razorpay_payment_id"),
            payload.get("razorpay_signature")
    );
}

}
