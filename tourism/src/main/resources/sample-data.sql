-- ============================================================
-- Sample data for Tourism project (MySQL)
-- ============================================================
-- HOW TO RUN:
-- 1. Create database: CREATE DATABASE IF NOT EXISTS tourism;
-- 2. Start the app once so Hibernate creates tables (ddl-auto=update).
-- 3. Run this script ONCE in MySQL:
--    mysql -u root -p tourism < sample-data.sql
--    Or in MySQL Workbench: File -> Run SQL Script -> select this file.
-- ============================================================
-- Test login: email = admin@tourism.com, password = password
-- (BCrypt hash below is for "password")
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. States
INSERT INTO state (state_name) VALUES
('Rajasthan'),
('Uttar Pradesh'),
('Tamil Nadu'),
('Kerala'),
('Goa');

-- 2. Cities
INSERT INTO city (city_name, state_id) VALUES
('Jaipur', 1), ('Udaipur', 1), ('Jodhpur', 1),
('Agra', 2), ('Varanasi', 2),
('Chennai', 3), ('Madurai', 3),
('Kochi', 4), ('Munnar', 4),
('Panaji', 5);

-- 3. Places (is_asi_monument: 1=yes, 0=no; entry_fee in INR)
INSERT INTO place (place_name, is_asi_monument, entry_fee, city_id) VALUES
('Amer Fort', 1, 500.00, 1),
('Hawa Mahal', 1, 200.00, 1),
('City Palace, Jaipur', 0, 400.00, 1),
('Lake Palace', 0, 0.00, 2),
('City Palace, Udaipur', 0, 300.00, 2),
('Mehrangarh Fort', 1, 600.00, 3),
('Jaswant Thada', 0, 50.00, 3),
('Taj Mahal', 1, 1100.00, 4),
('Agra Fort', 1, 550.00, 4),
('Kashi Vishwanath Temple', 0, 0.00, 5),
('Sarnath', 1, 500.00, 5),
('Kapaleeshwarar Temple', 0, 0.00, 6),
('Marina Beach', 0, 0.00, 6),
('Meenakshi Temple', 1, 50.00, 7),
('Fort Kochi', 0, 0.00, 8),
('Chinese Fishing Nets', 0, 0.00, 8),
('Tea Gardens Munnar', 0, 100.00, 9),
('Eravikulam National Park', 0, 125.00, 9),
('Calangute Beach', 0, 0.00, 10),
('Basilica of Bom Jesus', 1, 50.00, 10);

-- 4. Users (password = "password" in BCrypt)
INSERT INTO users (name, email, password, created_at) VALUES
('Admin User', 'admin@tourism.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW()),
('Priya Sharma', 'priya@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW()),
('Rahul Verma', 'rahul@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NOW());

-- 5. Hotels
INSERT INTO hotel (name, address, rating, city_name) VALUES
('Rambagh Palace', 'Bhawani Singh Rd, Jaipur', 4.8, 'Jaipur'),
('Taj Lake Palace', 'Lake Pichola, Udaipur', 4.9, 'Udaipur'),
('Umaid Bhawan Palace', 'Jodhpur', 4.9, 'Jodhpur'),
('Oberoi Amarvilas', 'Taj East Gate Rd, Agra', 4.9, 'Agra'),
('Taj Gateway Resort', 'Munnar', 4.5, 'Munnar'),
('Holiday Inn', 'Panaji, Goa', 4.2, 'Panaji');

-- 6. Hotel Rooms
INSERT INTO hotel_room (room_type, price_per_night, hotel_id) VALUES
('SINGLE', 4500.00, 1), ('DOUBLE', 7500.00, 1), ('DELUXE', 12000.00, 1),
('SINGLE', 8500.00, 2), ('DOUBLE', 15000.00, 2), ('DELUXE', 25000.00, 2),
('SINGLE', 6000.00, 3), ('DOUBLE', 10000.00, 3),
('SINGLE', 3500.00, 4), ('DOUBLE', 5500.00, 4),
('SINGLE', 4000.00, 5), ('DOUBLE', 6000.00, 5),
('SINGLE', 2500.00, 6), ('DOUBLE', 3500.00, 6);

-- 7. Trips
INSERT INTO trip (budget, travel_type, pace, start_time, user_id) VALUES
('MEDIUM', 'FAMILY', 'MODERATE', '09:00', 1),
('HIGH', 'COUPLE', 'SLOW', '08:00', 2),
('LOW', 'SOLO', 'FAST', '07:00', 3);

-- 8. Trip Days
INSERT INTO trip_day (day_number, trip_id) VALUES
(1, 1), (2, 1), (3, 1),
(1, 2), (2, 2),
(1, 3);

-- 9. Trip Schedules
INSERT INTO trip_schedule (time, label, day_id) VALUES
('09:00 – 11:00', 'Amer Fort', 1),
('11:30 – 13:30', 'Hawa Mahal', 1),
('14:00 – 15:00', 'Lunch Break', 1),
('15:30 – 17:30', 'City Palace', 1),
('09:00 – 11:00', 'Lake Palace', 2),
('11:30 – 13:30', 'City Palace Udaipur', 2),
('08:00 – 10:00', 'Mehrangarh Fort', 3),
('10:30 – 11:30', 'Jaswant Thada', 3),
('08:00 – 10:00', 'Taj Mahal', 4),
('10:30 – 12:30', 'Agra Fort', 4),
('07:00 – 09:00', 'Tea Gardens', 5),
('09:30 – 11:30', 'Eravikulam Park', 5);

-- 10. Bookings
INSERT INTO booking (user_id, trip_id, hotel_amount, asi_ticket_amount, total_amount, status, created_at) VALUES
(1, 1, 15000.00, 1100.00, 16100.00, 'CONFIRMED', NOW()),
(2, 2, 25000.00, 1150.00, 26150.00, 'PENDING', NOW()),
(3, 3, 8000.00, 500.00, 8500.00, 'CONFIRMED', NOW());

-- 11. Booking Tickets
INSERT INTO booking_ticket (booking_id, place_id, price) VALUES
(1, 1, 500.00), (1, 2, 200.00), (1, 3, 400.00),
(2, 8, 1100.00), (2, 9, 550.00),
(3, 17, 100.00), (3, 18, 125.00);

-- 12. Payments
INSERT INTO payments (booking_id, razorpay_order_id, razorpay_payment_id, razorpay_signature, amount, status, created_at) VALUES
(1, 'order_confirm_1', 'pay_confirm_1', 'sig_1', 16100.00, 'SUCCESS', NOW()),
(2, 'order_pend_2', NULL, NULL, 26150.00, 'PENDING', NOW()),
(3, 'order_confirm_3', 'pay_confirm_3', 'sig_3', 8500.00, 'SUCCESS', NOW());

SET FOREIGN_KEY_CHECKS = 1;
