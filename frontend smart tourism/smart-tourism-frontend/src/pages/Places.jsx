import { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getPlaces } from "../api/placeApi";
import { TripContext } from "../context/TripContext";
import Breadcrumb from "../components/Breadcrumb";
import SelectedPlaces from "../components/SelectedPlaces";

const Places = () => {
  const { cityId } = useParams();
  const navigate = useNavigate();

  const [places, setPlaces] = useState([]);
  const [loading, setLoading] = useState(true);

  const { selectedPlaces, addPlace, removePlace } =
    useContext(TripContext);

  useEffect(() => {
    let isMounted = true;

    getPlaces(cityId)
      .then((res) => {
        if (isMounted) {
          setPlaces(res.data);
        }
      })
      .catch((err) => {
        console.error("Error fetching places", err);
      })
      .finally(() => {
        if (isMounted) {
          setLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [cityId]);

  const isSelected = (placeId) =>
    selectedPlaces.some(p => p.placeId === placeId);

  // 🔄 Loading state
  if (loading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" />
        <p className="mt-2">Loading places...</p>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      {/* Breadcrumb */}
      <Breadcrumb
        items={[
          { label: "States", path: "/states" },
          { label: "Cities", path: "#" },
          { label: "Places", active: true }
        ]}
      />

      <h3 className="text-center mb-4">Select Places</h3>

      {/* Empty state */}
      {places.length === 0 && (
        <div className="alert alert-info text-center">
          No places available for this city.
        </div>
      )}

      <div className="row">
        {/* Places list */}
        <div className="col-md-8">
          <div className="row">
            {places.map((place) => (
              <div key={place.placeId} className="col-md-6 mb-4">
                <div className="card h-100 shadow-sm">
                  <div className="card-body d-flex flex-column">
                    <h5 className="card-title fw-bold">
                      {place.placeName}
                    </h5>

                    <p className="text-muted mb-2">
                      Entry Fee: ₹{place.entryFee}
                    </p>

                    {place.isAsiMonument && (
                      <span className="badge bg-warning text-dark mb-3">
                        ASI Monument
                      </span>
                    )}

                    <button
                      className={`btn mt-auto ${
                        isSelected(place.placeId)
                          ? "btn-outline-danger"
                          : "btn-outline-primary"
                      }`}
                      onClick={() =>
                        isSelected(place.placeId)
                          ? removePlace(place.placeId)
                          : addPlace(place)
                      }
                    >
                      {isSelected(place.placeId) ? "Remove" : "Add"}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Selected Places panel */}
        <div className="col-md-4">
          <SelectedPlaces />
        </div>
      </div>

      {/* CONTINUE → PREFERENCES */}
      <div className="text-center mt-4">
        <button
          className="btn btn-success px-4"
          disabled={selectedPlaces.length === 0}
          onClick={() => navigate("/preferences")}
        >
          Continue
        </button>
      </div>
    </div>
  );
};

export default Places;
