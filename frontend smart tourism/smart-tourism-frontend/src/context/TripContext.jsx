import { createContext, useState } from "react";

export const TripContext = createContext();

export const TripProvider = ({ children }) => {
  const [selectedPlaces, setSelectedPlaces] = useState([]);

  const [preferences, setPreferences] = useState({
    budget: "",
    travelType: "",
    interests: [],
    pace: "",
    startTime: ""
  });

  const addPlace = (place) => {
    setSelectedPlaces((prev) =>
      prev.some(p => p.placeId === place.placeId)
        ? prev
        : [...prev, place]
    );
  };

  const removePlace = (placeId) => {
    setSelectedPlaces((prev) =>
      prev.filter(p => p.placeId !== placeId)
    );
  };

  return (
    <TripContext.Provider
      value={{
        selectedPlaces,
        addPlace,
        removePlace,
        preferences,
        setPreferences
      }}
    >
      {children}
    </TripContext.Provider>
  );
};
