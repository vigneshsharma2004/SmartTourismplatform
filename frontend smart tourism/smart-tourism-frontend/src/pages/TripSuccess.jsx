import { useNavigate, useLocation } from "react-router-dom";

const TripSuccess = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const { tripId } = location.state || {};

  if (!tripId) {
    return (
      <div className="container mt-5 text-center">
        <h4>Invalid Access</h4>
        <p>No trip information found.</p>
      </div>
    );
  }

  return (
    <div className="container mt-5 text-center">
      <div className="card shadow-sm p-4">
        <h3 className="text-success mb-3">
          🎉 Trip Saved Successfully!
        </h3>

        <p className="mb-2">
          Your Trip ID:
        </p>

        <h5 className="fw-bold text-primary">
          {tripId}
        </h5>

        <div className="mt-4 d-flex justify-content-center gap-3">
          <button
            className="btn btn-primary"
            onClick={() => navigate(`/trips/${tripId}`)}
          >
            View Trip
          </button>

          <button
            className="btn btn-outline-secondary"
            onClick={() => navigate("/states")}
          >
            Plan Another Trip
          </button>
        </div>
      </div>
    </div>
  );
};

export default TripSuccess;
