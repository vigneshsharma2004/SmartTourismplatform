import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { generateAndSaveTrip, getTripById } from "../api/itineraryApi";
import { isAuthenticated } from "../api/authApi";

const Itinerary = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [loading, setLoading] = useState(true);
  const [itinerary, setItinerary] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    // 🔐 Safety check (route is protected, but double check)
    if (!isAuthenticated()) {
      navigate("/login", { state: { from: "/itinerary" } });
      return;
    }

    // Use location.state first; if missing (e.g. after login redirect or refresh), try sessionStorage
    let state = location.state;
    if (!state) {
      try {
        const stored = sessionStorage.getItem("tripDraft");
        if (stored) state = JSON.parse(stored);
      } catch (e) {
        /* ignore */
      }
    }

    if (!state || (!state.selectedPlaceIds?.length && !state.selectedPlaces?.length)) {
      setError("No itinerary data found. Please start again.");
      setLoading(false);
      return;
    }

    // Backend expects selectedPlaces: [{ placeId }, ...] and preferences
    const placeIds = state.selectedPlaceIds ?? state.selectedPlaces?.map((p) => p.placeId ?? p.place_id) ?? [];
    const payload = {
      selectedPlaces: placeIds.map((placeId) => ({ placeId })),
      preferences: state.preferences || {}
    };

    const generateTrip = async () => {
      try {
        const res = await generateAndSaveTrip(payload);
        const tripId = res.data;
        const tripRes = await getTripById(tripId);
        setItinerary({ ...tripRes.data, tripId });
        try {
          sessionStorage.removeItem("tripDraft");
        } catch (e) {
          /* ignore */
        }
      } catch (err) {
        console.error(err);
        setError("Failed to generate itinerary.");
      } finally {
        setLoading(false);
      }
    };

    generateTrip();
  }, [location.state, navigate]);

  if (loading) {
    return <p className="text-center mt-5">Generating itinerary...</p>;
  }

  if (error) {
    return (
      <div className="container mt-5 text-center">
        <p className="text-danger mb-3">{error}</p>
        <p className="text-muted small mb-3">
          Select places from States → Cities → Places, then set your preferences.
        </p>
        <button
          className="btn btn-primary"
          onClick={() => navigate("/states")}
        >
          Start again
        </button>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Your Itinerary</h2>

      {itinerary?.days?.map((day, index) => (
        <div key={index} className="card mb-3">
          <div className="card-body">
            <h5 className="card-title">Day {day.dayNumber ?? index + 1}</h5>

            <ul className="list-group list-group-flush">
              {(day.schedules || []).map((schedule, i) => (
                <li key={i} className="list-group-item">
                  {schedule.time} — {schedule.label}
                </li>
              ))}
            </ul>
          </div>
        </div>
      ))}

      {itinerary?.tripId && (
        <div className="text-center mt-4">
          <button
            className="btn btn-primary"
            onClick={() => navigate(`/trips/${itinerary.tripId}`)}
          >
            View Saved Trip
          </button>
        </div>
      )}
    </div>
  );
};

export default Itinerary;
