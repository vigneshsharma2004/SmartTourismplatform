import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getTripById } from "../api/itineraryApi";
import Breadcrumb from "../components/Breadcrumb";

const SavedTrip = () => {
  const { tripId } = useParams();

  const [trip, setTrip] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchTrip();
  }, []);

  const fetchTrip = async () => {
    try {
      const response = await getTripById(tripId);
      setTrip(response.data);
    } catch (err) {
      console.error(err);
      setError("Unable to load trip details.");
    } finally {
      setLoading(false);
    }
  };

  /* ---------- STATES ---------- */

  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" />
        <p className="mt-2">Loading trip...</p>
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

  if (!trip) {
    return (
      <div className="container mt-5 text-center">
        <h4>Trip not found</h4>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <Breadcrumb
        items={[
          { label: "Trips", path: "/states" },
          { label: `Trip ${tripId}`, active: true }
        ]}
      />

      <h3 className="text-center mb-4">
        Saved Trip Itinerary
      </h3>

      {/* ---------- ITINERARY ---------- */}
      {!trip.days || trip.days.length === 0 ? (
        <div className="alert alert-warning text-center">
          No itinerary data available for this trip.
        </div>
      ) : (
        trip.days.map(day => (
          <div
            key={day.dayNumber}
            className="card mb-4 shadow-sm"
          >
            <div className="card-body">
              <h5 className="fw-bold mb-3">
                Day {day.dayNumber}
              </h5>

              {!day.schedules || day.schedules.length === 0 ? (
                <p className="text-muted">
                  No activities planned for this day.
                </p>
              ) : (
                <ul className="list-group list-group-flush">
                  {day.schedules.map((schedule, index) => (
                    <li
                      key={`${day.dayNumber}-${index}`}
                      className="list-group-item d-flex justify-content-between"
                    >
                      <span>{schedule.label}</span>
                      <span className="text-muted">
                        {schedule.time}
                      </span>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </div>
        ))
      )}

      {/* ---------- SUMMARY ---------- */}
      <div className="card mt-4 shadow-sm">
        <div className="card-body">
          <h6 className="fw-bold mb-2">Trip Summary</h6>
          <p className="mb-1">
            <strong>Budget:</strong> {trip.budget || "-"}
          </p>
          <p className="mb-1">
            <strong>Travel Type:</strong> {trip.travelType || "-"}
          </p>
          <p className="mb-0">
            <strong>Pace:</strong> {trip.pace || "-"}
          </p>
        </div>
      </div>

      <div className="text-center mt-4">
        <Link
          to={`/trips/${tripId}/book`}
          className="btn btn-success btn-lg"
        >
          Book this trip
        </Link>
      </div>
    </div>
  );
};

export default SavedTrip;
  