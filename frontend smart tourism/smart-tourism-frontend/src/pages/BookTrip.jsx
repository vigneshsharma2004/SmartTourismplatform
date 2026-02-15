import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getHotelsByCity, getRoomsByHotel } from "../api/hotelApi";
import { createBooking } from "../api/bookingApi";
import { createOrder, verifyPayment } from "../api/paymentApi";
import Breadcrumb from "../components/Breadcrumb";

const RAZORPAY_KEY = import.meta.env.VITE_RAZORPAY_KEY || "rzp_test_xxxxx";

const BookTrip = () => {
  const { tripId } = useParams();
  const navigate = useNavigate();

  const [step, setStep] = useState(1);
  const [cityName, setCityName] = useState("");
  const [hotels, setHotels] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [selectedHotel, setSelectedHotel] = useState(null);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [nights, setNights] = useState(1);
  const [booking, setBooking] = useState(null);
  const [paymentSuccess, setPaymentSuccess] = useState(false);

  const fetchHotels = async () => {
    if (!cityName.trim()) {
      setError("Please enter a city name.");
      return;
    }
    setError("");
    setLoading(true);
    try {
      const res = await getHotelsByCity(cityName.trim());
      setHotels(res.data || []);
      if ((res.data || []).length === 0) setError("No hotels found for this city.");
    } catch (err) {
      setError(err.response?.data?.message || "Failed to load hotels.");
      setHotels([]);
    } finally {
      setLoading(false);
    }
  };

  const onSelectHotel = async (hotel) => {
    setSelectedHotel(hotel);
    setSelectedRoom(null);
    setRooms([]);
    setLoading(true);
    setError("");
    try {
      const res = await getRoomsByHotel(hotel.hotelId);
      setRooms(res.data || []);
    } catch (err) {
      setError("Failed to load rooms.");
      setRooms([]);
    } finally {
      setLoading(false);
    }
  };

  const createBookingAndPay = async () => {
    if (!selectedRoom || nights < 1) {
      setError("Please select a room and number of nights.");
      return;
    }
    setError("");
    setLoading(true);
    try {
      const bookRes = await createBooking({
        tripId: Number(tripId),
        hotelRoomId: selectedRoom.roomId,
        nights,
        selectedPlaceIds: [],
      });
      const bookingRecord = bookRes.data;
      setBooking(bookingRecord);

      const orderRes = await createOrder(bookingRecord.bookingId);
      const payment = orderRes.data;
      setStep(2);

      const options = {
        key: RAZORPAY_KEY,
        order_id: payment.razorpayOrderId,
        name: "Smart Tourism",
        description: `Booking for Trip #${tripId}`,
        handler: async (response) => {
          try {
            await verifyPayment({
              razorpay_order_id: response.razorpay_order_id,
              razorpay_payment_id: response.razorpay_payment_id,
              razorpay_signature: response.razorpay_signature,
            });
            setPaymentSuccess(true);
          } catch (err) {
            setError("Payment verification failed.");
          }
        },
      };
      const rzp = new window.Razorpay(options);
      rzp.on("payment.failed", () => setError("Payment failed."));
      rzp.open();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to create booking or order.");
    } finally {
      setLoading(false);
    }
  };

  if (paymentSuccess) {
    return (
      <div className="container mt-4">
        <Breadcrumb
          items={[
            { label: "Trips", path: "/my-trips" },
            { label: `Trip ${tripId}`, path: `/trips/${tripId}` },
            { label: "Book", active: true },
          ]}
        />
        <div className="card shadow-sm p-4 text-center mt-4">
          <h4 className="text-success mb-3">Booking confirmed</h4>
          <p className="mb-2">Your stay and payment are confirmed.</p>
          <div className="d-flex justify-content-center gap-2 mt-3">
            <button
              className="btn btn-primary"
              onClick={() => navigate(`/trips/${tripId}`)}
            >
              View Trip
            </button>
            <button
              className="btn btn-outline-secondary"
              onClick={() => navigate("/my-trips")}
            >
              My Trips
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <Breadcrumb
        items={[
          { label: "Trips", path: "/my-trips" },
          { label: `Trip ${tripId}`, path: `/trips/${tripId}` },
          { label: "Book", active: true },
        ]}
      />
      <h3 className="mb-4">Book your stay</h3>

      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {step === 1 && (
        <>
          <div className="card shadow-sm mb-4">
            <div className="card-body">
              <label className="form-label fw-bold">City for stay</label>
              <div className="d-flex gap-2">
                <input
                  type="text"
                  className="form-control"
                  placeholder="e.g. Jaipur"
                  value={cityName}
                  onChange={(e) => setCityName(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && fetchHotels()}
                />
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={fetchHotels}
                  disabled={loading}
                >
                  {loading ? "Searching…" : "Search hotels"}
                </button>
              </div>
            </div>
          </div>

          {hotels.length > 0 && (
            <div className="card shadow-sm mb-4">
              <div className="card-header fw-bold">Select hotel</div>
              <ul className="list-group list-group-flush">
                {hotels.map((h) => (
                  <li
                    key={h.hotelId}
                    className={`list-group-item list-group-item-action ${selectedHotel?.hotelId === h.hotelId ? "active" : ""}`}
                    onClick={() => onSelectHotel(h)}
                    style={{ cursor: "pointer" }}
                  >
                    <strong>{h.name}</strong>
                    {h.rating != null && (
                      <span className="ms-2 text-muted">★ {h.rating}</span>
                    )}
                    {h.address && (
                      <div className="small text-muted">{h.address}</div>
                    )}
                  </li>
                ))}
              </ul>
            </div>
          )}

          {selectedHotel && (
            <div className="card shadow-sm mb-4">
              <div className="card-header fw-bold">Select room — {selectedHotel.name}</div>
              {loading && rooms.length === 0 ? (
                <div className="card-body">Loading rooms…</div>
              ) : rooms.length === 0 ? (
                <div className="card-body text-muted">No rooms available.</div>
              ) : (
                <ul className="list-group list-group-flush">
                  {rooms.map((r) => (
                    <li
                      key={r.roomId}
                      className={`list-group-item list-group-item-action d-flex justify-content-between align-items-center ${selectedRoom?.roomId === r.roomId ? "active" : ""}`}
                      onClick={() => setSelectedRoom(r)}
                      style={{ cursor: "pointer" }}
                    >
                      <span>{r.roomType}</span>
                      <span>₹{r.pricePerNight}/night</span>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}

          {selectedRoom && (
            <div className="card shadow-sm mb-4">
              <div className="card-body">
                <label className="form-label fw-bold">Number of nights</label>
                <input
                  type="number"
                  min={1}
                  className="form-control w-25"
                  value={nights}
                  onChange={(e) => setNights(Number(e.target.value) || 1)}
                />
                <p className="mt-2 mb-0 text-muted">
                  Total: ₹{(selectedRoom.pricePerNight * nights).toFixed(0)}
                </p>
                <button
                  className="btn btn-success mt-3"
                  onClick={createBookingAndPay}
                  disabled={loading}
                >
                  {loading ? "Creating…" : "Proceed to payment"}
                </button>
              </div>
            </div>
          )}
        </>
      )}

      {step === 2 && !paymentSuccess && (
        <div className="card shadow-sm p-4 text-center">
          <p>Complete payment in the Razorpay window. If it closed, go back and try again.</p>
          <button className="btn btn-outline-primary" onClick={() => setStep(1)}>
            Back to booking
          </button>
        </div>
      )}
    </div>
  );
};

export default BookTrip;
