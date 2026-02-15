import axios from "./axiosConfig";

const BASE_URL = "/api/trips";

export const generateAndSaveTrip = (payload) => {
  return axios.post(`${BASE_URL}/generate-and-save`, payload);
};

export const getTripById = (tripId) => {
  return axios.get(`${BASE_URL}/${tripId}`);
};

export const getMyTrips = () => {
  return axios.get(`${BASE_URL}/my`);
};

export const deleteTrip = (tripId) => {
  return axios.delete(`${BASE_URL}/${tripId}`);
};
