import { useContext } from "react";
import { TripContext } from "../context/TripContext";

const SelectedPlaces = () => {
  const { selectedPlaces } = useContext(TripContext);

  if (selectedPlaces.length === 0) return null;

  return (
    <div className="card shadow-sm">
      <div className="card-body">
        <h6 className="fw-bold mb-2">Selected Places</h6>

        <ul className="list-group list-group-flush">
          {selectedPlaces.map(place => (
            <li key={place.placeId} className="list-group-item px-0">
              {place.placeName}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default SelectedPlaces;
