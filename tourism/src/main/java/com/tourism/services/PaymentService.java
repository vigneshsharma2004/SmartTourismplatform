package com.tourism.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.tourism.entity.Booking;
import com.tourism.entity.Payment;
import com.tourism.repository.BookingRepository;
import com.tourism.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final String razorpayKey;
    private final String razorpaySecret;

    public PaymentService(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            @Value("${razorpay.key}") String razorpayKey,
            @Value("${razorpay.secret}") String razorpaySecret
    ) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.razorpayKey = razorpayKey;
        this.razorpaySecret = razorpaySecret;
    }

    public Payment createRazorpayOrder(Long bookingId) throws Exception {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (razorpayKey == null || razorpayKey.contains("xxxxx")
                || razorpaySecret == null || razorpaySecret.contains("yyyyy")) {
            throw new IllegalStateException(
                "Razorpay keys not configured. Set razorpay.key and razorpay.secret in application.properties (get test keys from https://dashboard.razorpay.com/app/keys)."
            );
        }

        RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject options = new JSONObject();
        options.put("amount", (long) Math.round(booking.getTotalAmount() * 100)); // paise, integer
        options.put("currency", "INR");
        options.put("receipt", "booking_" + bookingId);

        Order order = client.orders.create(options);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setRazorpayOrderId(order.get("id"));
        payment.setStatus("PENDING");

        return paymentRepository.save(payment);
    }
    public void verifyPayment(
        String razorpayOrderId,
        String razorpayPaymentId,
        String razorpaySignature
) throws Exception {

    Payment payment = paymentRepository
            .findAll()
            .stream()
            .filter(p -> p.getRazorpayOrderId().equals(razorpayOrderId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Payment not found"));

    JSONObject options = new JSONObject();
    options.put("razorpay_order_id", razorpayOrderId);
    options.put("razorpay_payment_id", razorpayPaymentId);
    options.put("razorpay_signature", razorpaySignature);

    boolean valid = Utils.verifyPaymentSignature(
            options,
            razorpaySecret
    );

    if (!valid) {
        payment.setStatus("FAILED");
        paymentRepository.save(payment);
        throw new RuntimeException("Payment verification failed");
    }

    payment.setRazorpayPaymentId(razorpayPaymentId);
    payment.setRazorpaySignature(razorpaySignature);
    payment.setStatus("SUCCESS");

    payment.getBooking().setStatus("CONFIRMED");

    paymentRepository.save(payment);
}
}
