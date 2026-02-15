import axios from "./axiosConfig";

const BASE = "/api/hotels";

export const getHotelsByCity = (cityName) => {
  return axios.get(`${BASE}/city/${encodeURIComponent(cityName)}`);
};

export const getRoomsByHotel = (hotelId) => {
  return axios.get(`${BASE}/${hotelId}/rooms`);
};
