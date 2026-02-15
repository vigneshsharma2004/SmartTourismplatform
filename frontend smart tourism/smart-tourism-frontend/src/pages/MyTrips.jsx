import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMyTrips, deleteTrip } from "../api/itineraryApi";
import { isAuthenticated } from "../api/authApi";

const MyTrips = () => {
  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const navigate = useNavigate();

  /* 🔐 Redirect if not logged in */
  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    }
  }, [navigate]);

  /* 📡 Fetch trips */
  const fetchTrips = async () => {
    try {
      const res = await getMyTrips();
      setTrips(res.data);
    } catch (err) {
      console.error(err);
      setError("Failed to load trips");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTrips();
  }, []);

  /* 🗑️ Delete trip */
  const handleDelete = async (tripId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this trip?"
    );

    if (!confirmDelete) return;

    try {
      await deleteTrip(tripId);
      setTrips(prev =>
        prev.filter(trip => trip.tripId !== tripId)
      );
    } catch (err) {
      console.error(err);
      alert("Failed to delete trip");
    }
  };

  /* ---------- STATES ---------- */

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" />
        <p className="mt-2">Loading trips...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mt-5 text-center">
        <div className="alert alert-danger">{error}</div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h3 className="text-center mb-4">My Trips</h3>

      {trips.length === 0 ? (
        <p className="text-center text-muted">
          No trips found.
        </p>
      ) : (
        trips.map(trip => (
          <div
            key={trip.tripId}
            className="card mb-3 shadow-sm"
          >
            <div className="card-body d-flex justify-content-between align-items-center">
              <div>
                <p className="mb-1">
                  <strong>Travel Type:</strong> {trip.travelType}
                </p>
                <p className="mb-1">
                  <strong>Pace:</strong> {trip.pace}
                </p>
                <p className="mb-0">
                  <strong>Budget:</strong> {trip.budget}
                </p>
              </div>

              <div className="d-flex gap-2">
                <button
                  className="btn btn-outline-primary"
                  onClick={() =>
                    navigate(`/trips/${trip.tripId}`)
                  }
                >
                  View
                </button>

                <button
                  className="btn btn-outline-danger"
                  onClick={() =>
                    handleDelete(trip.tripId)
                  }
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default MyTrips;
