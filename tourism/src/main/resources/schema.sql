-- ============================================================
-- Smart Tourism - MySQL Schema
-- Database: tourism (JDBC: jdbc:mysql://localhost:3306/tourism)
-- ============================================================

CREATE DATABASE IF NOT EXISTS tourism
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE tourism;

-- ------------------------------------------------------------
-- Reference data: states and cities
-- ------------------------------------------------------------

CREATE TABLE state (
  state_id   INT AUTO_INCREMENT PRIMARY KEY,
  state_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE city (
  city_id   INT AUTO_INCREMENT PRIMARY KEY,
  city_name VARCHAR(255) NOT NULL,
  state_id  INT NOT NULL,
  CONSTRAINT fk_city_state FOREIGN KEY (state_id) REFERENCES state (state_id)
);

-- ------------------------------------------------------------
-- Places (per city)
-- ------------------------------------------------------------

CREATE TABLE place (
  place_id       INT AUTO_INCREMENT PRIMARY KEY,
  place_name     VARCHAR(255) NOT NULL,
  is_asi_monument TINYINT(1) DEFAULT NULL,
  entry_fee      DECIMAL(19, 2) DEFAULT NULL,
  city_id        INT NOT NULL,
  CONSTRAINT fk_place_city FOREIGN KEY (city_id) REFERENCES city (city_id)
);

-- ------------------------------------------------------------
-- Users (auth)
-- ------------------------------------------------------------

CREATE TABLE users (
  user_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) DEFAULT NULL,
  email      VARCHAR(255) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  created_at DATETIME(6) DEFAULT NULL
);

-- ------------------------------------------------------------
-- Trips and itinerary (user-generated)
-- ------------------------------------------------------------

CREATE TABLE trip (
  trip_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  budget      VARCHAR(255) DEFAULT NULL,
  travel_type VARCHAR(255) DEFAULT NULL,
  pace        VARCHAR(255) DEFAULT NULL,
  start_time  VARCHAR(255) DEFAULT NULL,
  user_id     BIGINT DEFAULT NULL,
  CONSTRAINT fk_trip_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE trip_day (
  day_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  day_number INT NOT NULL,
  trip_id    BIGINT DEFAULT NULL,
  CONSTRAINT fk_trip_day_trip FOREIGN KEY (trip_id) REFERENCES trip (trip_id) ON DELETE CASCADE
);

CREATE TABLE trip_schedule (
  schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  time        VARCHAR(255) DEFAULT NULL,
  label       VARCHAR(255) DEFAULT NULL,
  day_id      BIGINT DEFAULT NULL,
  CONSTRAINT fk_schedule_day FOREIGN KEY (day_id) REFERENCES trip_day (day_id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Hotels (optional / future use)
-- ------------------------------------------------------------

CREATE TABLE hotel (
  hotel_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) DEFAULT NULL,
  address    VARCHAR(255) DEFAULT NULL,
  rating     DOUBLE DEFAULT NULL,
  city_name  VARCHAR(255) DEFAULT NULL
);

CREATE TABLE hotel_room (
  room_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  room_type      VARCHAR(255) DEFAULT NULL,
  price_per_night DOUBLE DEFAULT NULL,
  hotel_id       BIGINT DEFAULT NULL,
  CONSTRAINT fk_room_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (hotel_id)
);

-- ------------------------------------------------------------
-- Bookings and payments
-- ------------------------------------------------------------

CREATE TABLE booking (
  booking_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id           BIGINT DEFAULT NULL,
  trip_id           BIGINT DEFAULT NULL,
  hotel_amount      DOUBLE DEFAULT NULL,
  asi_ticket_amount DOUBLE DEFAULT NULL,
  total_amount      DOUBLE DEFAULT NULL,
  status            VARCHAR(255) DEFAULT 'PENDING',
  created_at        DATETIME(6) DEFAULT NULL,
  CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT fk_booking_trip FOREIGN KEY (trip_id) REFERENCES trip (trip_id)
);

CREATE TABLE booking_ticket (
  ticket_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id BIGINT DEFAULT NULL,
  place_id   INT DEFAULT NULL,
  price      DOUBLE DEFAULT NULL,
  CONSTRAINT fk_ticket_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id),
  CONSTRAINT fk_ticket_place FOREIGN KEY (place_id) REFERENCES place (place_id)
);

CREATE TABLE payments (
  payment_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id          BIGINT NOT NULL,
  razorpay_order_id   VARCHAR(255) NOT NULL,
  razorpay_payment_id VARCHAR(255) DEFAULT NULL,
  razorpay_signature  VARCHAR(255) DEFAULT NULL,
  amount              DOUBLE NOT NULL,
  status              VARCHAR(255) NOT NULL DEFAULT 'PENDING',
  created_at          DATETIME(6) DEFAULT NULL,
  CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id)
);

-- ------------------------------------------------------------
-- Optional: indexes for common queries
-- ------------------------------------------------------------

CREATE INDEX idx_trip_user ON trip (user_id);
CREATE INDEX idx_place_city ON place (city_id);
CREATE INDEX idx_city_state ON city (state_id);
CREATE INDEX idx_booking_user ON booking (user_id);
CREATE INDEX idx_booking_trip ON booking (trip_id);
