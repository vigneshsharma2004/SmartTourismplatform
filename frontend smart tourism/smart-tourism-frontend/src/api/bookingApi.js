import axios from "./axiosConfig";

const BASE = "/api/bookings";

/**
 * @param {{ tripId: number, hotelRoomId: number, nights: number, selectedPlaceIds?: number[] }} payload
 */
export const createBooking = (payload) => {
  return axios.post(BASE, {
    tripId: payload.tripId,
    hotelRoomId: payload.hotelRoomId,
    nights: payload.nights,
    selectedPlaceIds: payload.selectedPlaceIds ?? [],
  });
};
