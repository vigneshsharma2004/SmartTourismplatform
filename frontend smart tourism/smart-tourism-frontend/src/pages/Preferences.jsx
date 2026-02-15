import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { TripContext } from "../context/TripContext";
import Breadcrumb from "../components/Breadcrumb";
import { isAuthenticated } from "../api/authApi";

const Preferences = () => {
  const navigate = useNavigate();

  const { preferences, setPreferences, selectedPlaces } =
    useContext(TripContext);

  const handleChange = (field, value) => {
    setPreferences(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const toggleInterest = (interest) => {
    setPreferences(prev => ({
      ...prev,
      interests: prev.interests.includes(interest)
        ? prev.interests.filter(i => i !== interest)
        : [...prev.interests, interest]
    }));
  };

  const isFormValid =
    preferences.budget &&
    preferences.travelType &&
    preferences.pace;

  const getTripDraft = () => ({
    selectedPlaceIds: selectedPlaces.map(p => p.placeId),
    preferences: {
      budget: preferences.budget,
      travelType: preferences.travelType,
      pace: preferences.pace,
      interests: preferences.interests || [],
      startTime: preferences.startTime
    }
  });

  const handleContinue = () => {
    const draft = getTripDraft();
    const draftKey = "tripDraft";
    try {
      sessionStorage.setItem(draftKey, JSON.stringify(draft));
    } catch (e) {
      /* ignore */
    }

    if (!isAuthenticated()) {
      navigate("/login", { state: { from: "/itinerary" } });
      return;
    }

    navigate("/itinerary", { state: draft });
  };

  return (
    <div className="container mt-4">
      <Breadcrumb
        items={[
          { label: "Places", path: "/states" },
          { label: "Preferences", active: true }
        ]}
      />

      <h3 className="text-center mb-4">
        Trip Preferences
      </h3>

      <div className="row">
        {/* LEFT: FORM */}
        <div className="col-md-8">
          <div className="card shadow-sm mb-4">
            <div className="card-body">

              <h5 className="fw-bold mb-3">
                Customize Your Trip
              </h5>

              {/* Budget */}
              <div className="mb-3">
                <label className="form-label fw-semibold">
                  Budget
                </label>
                <select
                  className="form-select"
                  value={preferences.budget}
                  onChange={(e) =>
                    handleChange("budget", e.target.value)
                  }
                >
                  <option value="">Select budget</option>
                  <option value="LOW">Low</option>
                  <option value="MEDIUM">Medium</option>
                  <option value="HIGH">High</option>
                </select>
              </div>

              {/* Travel Type */}
              <div className="mb-3">
                <label className="form-label fw-semibold">
                  Travel Type
                </label>
                <select
                  className="form-select"
                  value={preferences.travelType}
                  onChange={(e) =>
                    handleChange("travelType", e.target.value)
                  }
                >
                  <option value="">Select travel type</option>
                  <option value="SOLO">Solo</option>
                  <option value="FAMILY">Family</option>
                  <option value="FRIENDS">Friends</option>
                </select>
              </div>

              {/* Pace */}
              <div className="mb-3">
                <label className="form-label fw-semibold">
                  Travel Pace
                </label>
                <select
                  className="form-select"
                  value={preferences.pace}
                  onChange={(e) =>
                    handleChange("pace", e.target.value)
                  }
                >
                  <option value="">Select pace</option>
                  <option value="RELAXED">Relaxed</option>
                  <option value="MODERATE">Moderate</option>
                  <option value="FAST">Fast</option>
                </select>
              </div>

              {/* Interests */}
              <div className="mb-3">
                <label className="form-label fw-semibold">
                  Interests
                </label>

                <div className="d-flex flex-wrap gap-3">
                  {["HISTORY", "NATURE", "ADVENTURE", "SHOPPING"].map(i => (
                    <div key={i} className="form-check">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        checked={preferences.interests.includes(i)}
                        onChange={() => toggleInterest(i)}
                      />
                      <label className="form-check-label">
                        {i}
                      </label>
                    </div>
                  ))}
                </div>
              </div>

              {/* Start Time */}
              <div className="mb-4">
                <label className="form-label fw-semibold">
                  Preferred Start Time
                </label>
                <input
                  type="time"
                  className="form-control"
                  value={preferences.startTime}
                  onChange={(e) =>
                    handleChange("startTime", e.target.value)
                  }
                />
              </div>

              {/* Continue */}
              <button
                className="btn btn-success px-4"
                disabled={!isFormValid}
                onClick={handleContinue}
              >
                Continue
              </button>

              {!isFormValid && (
                <p className="text-danger mt-2">
                  Please select Budget, Travel Type, and Pace.
                </p>
              )}
            </div>
          </div>
        </div>

        {/* RIGHT: SUMMARY */}
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h6 className="fw-bold mb-3">
                Trip Summary
              </h6>

              <p className="text-muted mb-1">
                Selected Places
              </p>

              <ul className="list-group list-group-flush mb-3">
                {selectedPlaces.map(p => (
                  <li
                    key={p.placeId}
                    className="list-group-item px-0"
                  >
                    {p.placeName}
                  </li>
                ))}
              </ul>

              <p className="text-muted mb-1">
                Preferences
              </p>

              <ul className="list-unstyled mb-0">
                <li><strong>Budget:</strong> {preferences.budget || "-"}</li>
                <li><strong>Type:</strong> {preferences.travelType || "-"}</li>
                <li><strong>Pace:</strong> {preferences.pace || "-"}</li>
              </ul>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};

export default Preferences;
