import axios from "./axiosConfig";

export const getPlaces = (cityId) => {
  return axios.get("/places", { params: { cityId } });
};
