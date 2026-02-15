# Smart Tourism – API Contract

**Base URL:** `http://localhost:7778`  
**Content-Type:** `application/json` (for request bodies)  
**Authentication:** Protected endpoints require header: `Authorization: Bearer <JWT_TOKEN>`

---

## 1. Authentication

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/auth/signup` | No | Register a new user |
| POST | `/api/auth/login` | No | Login and get JWT |

### POST `/api/auth/signup`

**Request body:**
```json
{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

**Response:** `200 OK`
```json
{
  "token": "string (JWT)",
  "email": "string"
}
```

---

### POST `/api/auth/login`

**Request body:**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response:** `200 OK`
```json
{
  "token": "string (JWT)",
  "email": "string"
}
```

**Error:** `401` – invalid credentials (body may vary).

---

## 2. Explore (States, Cities, Places)

All explore endpoints are **public** (no auth).

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/states` | No | List all states |
| GET | `/cities?stateId={stateId}` | No | List cities in a state |
| GET | `/places?cityId={cityId}` | No | List places in a city |

### GET `/states`

**Query params:** None.

**Response:** `200 OK`
```json
[
  {
    "stateId": 1,
    "stateName": "Rajasthan"
  }
]
```

---

### GET `/cities`

**Query params:**

| Name     | Type    | Required | Description   |
|----------|---------|----------|---------------|
| stateId  | integer | Yes      | State ID      |

**Example:** `GET /cities?stateId=1`

**Response:** `200 OK`
```json
[
  {
    "cityId": 1,
    "cityName": "Jaipur"
  }
]
```

---

### GET `/places`

**Query params:**

| Name   | Type    | Required | Description |
|--------|---------|----------|-------------|
| cityId | integer | Yes      | City ID     |

**Example:** `GET /places?cityId=1`

**Response:** `200 OK`
```json
[
  {
    "placeId": 1,
    "placeName": "Amer Fort",
    "isAsiMonument": true,
    "entryFee": 500.00
  }
]
```

---

## 3. Trips

All trip endpoints require **JWT**.

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/trips/generate-and-save` | Yes | Generate itinerary and save trip |
| GET | `/api/trips/my` | Yes | Get current user's trips |
| GET | `/api/trips/{tripId}` | Yes | Get trip by ID |
| DELETE | `/api/trips/{tripId}` | Yes | Delete trip (own only) |

### POST `/api/trips/generate-and-save`

**Request body:**
```json
{
  "selectedPlaces": [
    { "placeId": 1 }
  ],
  "preferences": {
    "budget": "LOW | MEDIUM | HIGH",
    "travelType": "SOLO | FAMILY | FRIENDS",
    "pace": "RELAXED | MODERATE | FAST",
    "startTime": "09:00"
  }
}
```

- `selectedPlaces`: at least one item with `placeId` (integer).
- `preferences`: required; `startTime` optional (defaults to "09:00").

**Response:** `200 OK`  
Body is the new **trip ID** (number), e.g. `5`.

**Errors:**
- `400` – invalid request (e.g. no places, null preferences).
- `401` / `403` – not authenticated or invalid token.

---

### GET `/api/trips/my`

**Response:** `200 OK`
```json
[
  {
    "tripId": 1,
    "budget": "MEDIUM",
    "travelType": "FAMILY",
    "pace": "MODERATE",
    "startTime": "09:00",
    "days": [
      {
        "dayNumber": 1,
        "schedules": [
          {
            "time": "09:00 – 11:00",
            "label": "Amer Fort"
          }
        ]
      }
    ]
  }
]
```

---

### GET `/api/trips/{tripId}`

**Path params:**

| Name   | Type | Description |
|--------|------|-------------|
| tripId | long | Trip ID     |

**Response:** `200 OK` – same shape as a single trip in the array above (object with `tripId`, `budget`, `travelType`, `pace`, `startTime`, `days`).

**Errors:** `404` – trip not found.

---

### DELETE `/api/trips/{tripId}`

**Path params:** `tripId` (long).

**Response:** `200 OK` – no body (void).

**Errors:** `403` / `404` – not owner or trip not found.

---

## 4. Bookings

Requires **JWT**.

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/bookings` | Yes | Create a booking for a trip |

### POST `/api/bookings`

**Request body:**
```json
{
  "tripId": 1,
  "hotelRoomId": 5,
  "nights": 2,
  "selectedPlaceIds": [1, 2]
}
```

| Field             | Type           | Required | Description                          |
|------------------|----------------|----------|--------------------------------------|
| tripId           | long           | Yes      | Trip to book                         |
| hotelRoomId      | long           | Yes      | Room ID from `/api/hotels/{id}/rooms`|
| nights           | integer        | Yes      | Number of nights                     |
| selectedPlaceIds | array of long  | No       | Place IDs for ASI ticket calculation (default: []) |

**Response:** `200 OK`
```json
{
  "bookingId": 4,
  "totalAmount": 15000.50
}
```

**Errors:** `400` / `404` – e.g. trip not found, room not found; `401`/`403` – auth.

---

## 5. Payments

Requires **JWT** (except verify can be called after payment; ensure auth if your design requires it).

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/payments/create-order/{bookingId}` | Yes | Create Razorpay order for a booking |
| POST | `/api/payments/verify` | Yes | Verify payment signature after Razorpay success |

### POST `/api/payments/create-order/{bookingId}`

**Path params:** `bookingId` (long).

**Response:** `200 OK`
```json
{
  "razorpayOrderId": "order_xxxx",
  "amount": 15000.50
}
```

**Errors:**
- `404` – booking not found.
- `503` – Razorpay not configured (e.g. placeholder keys), body: `{ "message": "..." }`.
- `500` – Razorpay API error.

---

### POST `/api/payments/verify`

**Request body:** (as returned by Razorpay checkout)
```json
{
  "razorpay_order_id": "string",
  "razorpay_payment_id": "string",
  "razorpay_signature": "string"
}
```

**Response:** `200 OK` – no body (void).

**Errors:** `400` / `500` – e.g. payment not found or signature verification failed.

---

## 6. Hotels

Require **JWT**.

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/api/hotels/city/{cityName}` | Yes | List hotels in a city |
| GET | `/api/hotels/{hotelId}/rooms` | Yes | List rooms of a hotel |

### GET `/api/hotels/city/{cityName}`

**Path params:** `cityName` (string), e.g. `Jaipur`. URL-encode if needed.

**Response:** `200 OK`
```json
[
  {
    "hotelId": 1,
    "name": "Rambagh Palace",
    "address": "Bhawani Singh Rd, Jaipur",
    "rating": 4.8,
    "cityName": "Jaipur"
  }
]
```

---

### GET `/api/hotels/{hotelId}/rooms`

**Path params:** `hotelId` (long).

**Response:** `200 OK`
```json
[
  {
    "roomId": 1,
    "roomType": "SINGLE",
    "pricePerNight": 4500.00
  }
]
```

*(Note: `hotel` may be omitted or a reference in the entity; client typically only needs `roomId`, `roomType`, `pricePerNight`.)*

---

## Summary: Auth by endpoint

| Scope        | Endpoints |
|-------------|-----------|
| **Public**  | `POST /api/auth/login`, `POST /api/auth/signup`, `GET /states`, `GET /cities`, `GET /places` |
| **JWT required** | `POST /api/trips/generate-and-save`, `GET /api/trips/my`, `GET /api/trips/{tripId}`, `DELETE /api/trips/{tripId}`, `POST /api/bookings`, `POST /api/payments/create-order/{bookingId}`, `POST /api/payments/verify`, `GET /api/hotels/city/{cityName}`, `GET /api/hotels/{hotelId}/rooms` |

---

## Error responses

- **401 Unauthorized** – Missing or invalid JWT (e.g. expired, wrong signature).
- **403 Forbidden** – Valid JWT but not allowed (e.g. deleting another user’s trip).
- **404 Not Found** – Resource not found (trip, booking, etc.).
- **500 Internal Server Error** – Server or external API (e.g. Razorpay) error.
- **503 Service Unavailable** – Used when Razorpay keys are not configured; body: `{ "message": "..." }`.

Error response body format may vary; typically JSON with a `message` or similar field.
