package com.tourism.dto;

import java.util.List;

public class ItineraryRequestDTO {

    private List<PlaceDTO> selectedPlaces;
    private PreferencesDTO preferences;

    public List<PlaceDTO> getSelectedPlaces() {
        return selectedPlaces;
    }

    public void setSelectedPlaces(List<PlaceDTO> selectedPlaces) {
        this.selectedPlaces = selectedPlaces;
    }

    public PreferencesDTO getPreferences() {
        return preferences;
    }

    public void setPreferences(PreferencesDTO preferences) {
        this.preferences = preferences;
    }
}
