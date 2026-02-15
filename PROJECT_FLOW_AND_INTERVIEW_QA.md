# Smart Tourism – Project Flow & Interview Q&A

## Part 1: Project Flow (How to Explain to Interviewer)

### 1. Project Overview (30-second pitch)

**Smart Tourism** is a full-stack web application where users can **explore destinations (states, cities, places)**, **build a trip** by selecting places and preferences, **get an AI-style day-wise itinerary**, **save trips** (after login), and **book a stay** with **hotel selection and Razorpay payment**.

- **Frontend:** React (Vite), React Router, Axios, Bootstrap, JWT in localStorage.
- **Backend:** Spring Boot, Spring Security (JWT), MySQL, Razorpay.
- **Auth:** JWT-based; protected routes (itinerary, my trips, book) require login.

---

### 2. End-to-End User Flow (Step-by-Step)

```
1. LANDING
   User opens app → Index/Home.

2. EXPLORE (Public)
   States → Cities (by state) → Places (by city).
   User selects multiple places (stored in TripContext).

3. PREFERENCES (Public)
   User sets: Budget, Travel Type, Pace, Interests, Start Time.
   Clicks "Continue".

4. AUTH CHECK
   - If NOT logged in → redirect to Login (trip draft saved in sessionStorage).
   - After login → redirect back to Itinerary (draft restored from sessionStorage).
   - If already logged in → go directly to Itinerary with state/draft.

5. ITINERARY (Protected)
   Backend generates day-wise schedule from selected places + preferences (pace = places per day, start time, lunch break).
   Trip is saved in DB and linked to user.
   User sees itinerary and can "View Saved Trip".

6. MY TRIPS (Protected)
   User sees list of their saved trips; can open or delete a trip.

7. SAVED TRIP (Protected)
   View full itinerary (days, schedules).
   Button: "Book this trip".

8. BOOK TRIP (Protected)
   - Enter city → fetch hotels → select hotel → fetch rooms → select room + nights.
   - "Proceed to payment" → Backend creates Booking (trip + room + nights + ASI ticket amount if any).
   - Backend creates Razorpay order → Frontend opens Razorpay Checkout.
   - User pays → Frontend sends payment IDs to backend → Backend verifies signature → Booking status = CONFIRMED.
   - Success screen with "View Trip" / "My Trips".
```

---

### 3. High-Level Architecture

```
[Browser]
   ↓
[React App – Vite]
   • Pages: States, Cities, Places, Preferences, Itinerary, My Trips, Saved Trip, Book Trip
   • TripContext: selectedPlaces, preferences (add/remove places, set preferences)
   • Axios: baseURL = backend, interceptors add Bearer token, 401/403 → logout
   • PrivateRoute: wraps protected pages, redirects to /login if not authenticated
   ↓ HTTP (REST + JWT)
[Spring Boot – Port 7778]
   • Security: JWT filter reads Authorization header, validates token, sets SecurityContext
   • Public: /api/auth/**, /states, /cities, /places
   • Protected: /api/trips/**, /api/bookings, /api/payments, /api/hotels
   ↓
[MySQL]
   • Tables: users, state, city, place, trip, trip_day, trip_schedule, hotel, hotel_room, booking, payments
```

---

### 4. Main Features in One Line Each

| Feature            | What it does |
|--------------------|--------------|
| Explore            | List states → cities by stateId → places by cityId (all from DB). |
| TripContext        | Holds selectedPlaces and preferences across States → Cities → Places → Preferences. |
| Preferences        | Budget, travel type, pace, interests, start time; navigates to Itinerary with state + saves draft in sessionStorage. |
| Itinerary          | POST selected places + preferences → backend generates schedule (pace = places/day, start time, lunch) and saves trip → returns tripId and days. |
| SessionStorage     | Trip draft saved before going to login; restored on Itinerary so redirect after login still has data. |
| My Trips           | GET user’s trips (JWT) → list; delete trip by id. |
| Saved Trip         | GET trip by id → show days and schedules; "Book this trip" → /trips/:id/book. |
| Book Trip          | Hotels by city → rooms by hotel → create booking (trip + room + nights) → create Razorpay order → Checkout → verify payment → confirm. |
| Razorpay           | Backend: create order (amount from booking total), verify signature; frontend: open Checkout with order_id, then call verify API. |

---

## Part 2: Interview Q&A

### Project & Role

**Q1: How would you describe this project in one minute?**  
Smart Tourism is a full-stack trip-planning and booking app. Users explore states, cities, and places, set preferences like budget and pace, and get a day-wise itinerary that is saved to their account. They can then book a stay by choosing a hotel and room and paying via Razorpay. I built the frontend in React with Vite and the backend in Spring Boot with JWT auth and MySQL.

**Q2: What was your role?**  
I implemented the full flow: frontend pages and routing, context for trip state, API integration with Axios, JWT handling and protected routes, and on the backend the REST APIs, security with JWT filter, itinerary generation, booking and payment with Razorpay, and persistence with Spring Data JPA and MySQL.

---

### Frontend (React)

**Q3: Why did you use React and Vite?**  
React for component-based UI and a clear data flow. Vite for fast dev server and builds and native ES modules.

**Q4: How is state managed? Where do you store selected places and preferences?**  
Selected places and preferences are in React Context (TripContext). That way States, Cities, Places, and Preferences can read/update the same data without prop drilling. For the itinerary step, we also persist a draft in sessionStorage so that after login redirect we still have the data.

**Q5: How do you handle authentication on the frontend?**  
We store the JWT in localStorage after login. Axios interceptors add the `Authorization: Bearer <token>` header to every request. For protected routes we use a PrivateRoute component that checks `isAuthenticated()` (token present and not expired via jwt-decode); if not authenticated, it redirects to `/login`. On 401/403 from the API we clear the token (logout) so the user is sent to login when they hit a protected route again.

**Q6: What are protected vs public routes?**  
Public: Home, Login, Signup, States, Cities, Places, Preferences. Protected (require login): Itinerary, My Trips, Saved Trip, Book Trip. PrivateRoute wraps the protected page and redirects to Login if the user is not authenticated.

**Q7: Why sessionStorage for the trip draft?**  
When the user clicks “Continue” on Preferences without being logged in, we redirect to Login and then back to Itinerary. React Router does not preserve location.state across that redirect. So we save the draft (selected place IDs and preferences) in sessionStorage before redirecting and restore it on the Itinerary page so the user doesn’t lose their choices.

---

### Backend (Spring Boot)

**Q8: Explain your REST API design.**  
We have resource-based endpoints: auth (login/signup), explore (states, cities, places), trips (generate-and-save, get by id, my trips, delete), bookings (create), payments (create-order, verify), hotels (by city, rooms by hotel). We use DTOs for request/response to avoid exposing entities and to control payload shape.

**Q9: How does itinerary generation work?**  
The client sends selected place IDs and preferences (budget, travel type, pace, start time). We load the places from the DB. Pace maps to places per day (e.g. RELAXED=2, MODERATE=3, FAST=4). We split places into days, assign time slots (e.g. 2 hours per place), insert a lunch break between 13:00–14:00 when needed, and return a list of day DTOs with schedule items. Then we persist the trip and its days/schedules and return the saved trip (or tripId) to the client.

**Q10: How do you secure your APIs?**  
Spring Security with a stateless JWT setup. A custom filter (JwtAuthFilter) runs before the Spring Security filter chain: it reads the `Authorization: Bearer <token>` header, validates the token using a shared secret (from configuration), and if valid sets an authentication in SecurityContext. Routes under `/api/auth/**`, `/states`, `/cities`, `/places` are permitted for all; `/api/trips/**`, `/api/bookings`, `/api/payments`, `/api/hotels` require authentication. CSRF is disabled because we use JWT, not session cookies.

**Q11: Why did you move the JWT secret to configuration?**  
Initially the signing key was generated at runtime, so every server restart invalidated all existing tokens and users got 403. Moving the secret to application.properties gives a stable key across restarts so tokens stay valid until expiry.

**Q12: How does the booking flow work on the backend?**  
Client sends tripId, hotelRoomId, nights, and optionally selectedPlaceIds (for ASI monument entry fees). We load the trip and room, compute hotel amount as pricePerNight × nights, and ASI amount from selected places that are ASI monuments. We create a Booking with user, trip, amounts, and status PENDING, then return bookingId and totalAmount so the client can create a Razorpay order.

**Q13: How is Razorpay integrated?**  
For create order: we get the booking, create a Razorpay order with amount in paise (integer), currency INR, and receipt. We store the Razorpay order ID in a Payment record (PENDING) and return orderId and amount to the frontend. For verify: frontend sends back order_id, payment_id, and signature; we look up the Payment, verify the signature with the Razorpay secret, then mark payment as SUCCESS and booking as CONFIRMED. Keys are in application.properties so we can use test/live keys without code changes.

---

### Database & Design

**Q14: What are the main entities and relationships?**  
User has many Trips. Trip has many TripDays; each TripDay has many TripSchedules (time + label). State has many Cities; City has many Places. Hotel has many HotelRooms; we find hotels by cityName. Booking belongs to User and Trip; Payment belongs to Booking. Place can be ASI monument with entry fee for booking cost calculation.

**Q15: Why use DTOs instead of returning entities?**  
T
o avoid over-fetching, circular references (e.g. Trip → User → Trips), and to keep the API contract stable. We use request DTOs (e.g. BookingRequestDTO) and response DTOs (e.g. TripResponseDTO, BookingResponseDTO, PaymentOrderDTO) so the JSON shape is explicit and safe.
---

### Troubleshooting & Decisions

**Q16: User got 403 on itinerary generation. What was the cause and fix?**  
The JWT signing key was generated randomly on each application start. After a restart, existing tokens failed validation, so the filter didn’t set authentication and Spring Security returned 403. Fix: use a fixed secret from application.properties (e.g. jwt.secret) so the same key is used across restarts.

**Q17: User saw “No itinerary data found” after login. Why?**  
They had gone from Preferences to Login (not logged in), then after login were redirected to Itinerary. location.state is lost on that redirect. Fix: save the trip draft (selected places + preferences) in sessionStorage before redirecting to login, and on the Itinerary page restore from sessionStorage when location.state is missing.

**Q18: Create order returned 500. What was wrong?**  
Razorpay keys were placeholders, so the Razorpay API call failed and the exception caused 500. We moved keys to application.properties, validated that they’re not placeholders before calling Razorpay (and throw a clear exception), and ensured amount is sent as an integer (paise). We also added an exception handler to return 503 with a clear “Razorpay not configured” message when keys are missing or invalid.

---

### Scalability & Improvements

**Q19: How would you scale or improve this project?**  
- **Caching:** Cache states/cities/places (or at least list APIs) with something like Redis or Spring Cache to reduce DB load.  
- **Validation:** Add @Valid and Bean Validation on DTOs (e.g. @NotNull, @Min) and return 400 with clear messages.  
- **Error handling:** Use @ControllerAdvice for consistent error response format and status codes.  
- **Pagination:** Use page/size for My Trips and for large place lists.  
- **Security:** Use HTTP-only cookie for token (if moving away from localStorage) and short-lived access token + refresh token.  
- **Testing:** Unit tests for ItineraryService, BookingService, PaymentService; integration tests for controllers; frontend tests for critical flows.

**Q20: How would you add “My Bookings” for the user?**  
Backend: add an endpoint like GET /api/bookings/my that uses the JWT user to fetch bookings (e.g. from BookingRepository.findByUserEmail). Return a list of booking DTOs (bookingId, trip summary, status, amount). Frontend: add a “My Bookings” link (e.g. in Navbar) and a page that calls this API and displays the list with filters (e.g. by status).

---

## Quick Reference: Tech Stack

| Layer    | Technology |
|----------|------------|
| Frontend | React 19, Vite 7, React Router 7, Axios, Bootstrap 5, jwt-decode |
| Backend  | Spring Boot 3, Spring Security (JWT), Spring Data JPA |
| Database | MySQL |
| Payment  | Razorpay (orders + signature verification) |
| Auth     | JWT (Bearer token in header), secret from application.properties |

Use this document to walk through the flow in an interview and to answer project-specific and technical questions confidently.
