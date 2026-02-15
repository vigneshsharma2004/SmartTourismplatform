import axios from "./axiosConfig";

export const getCities = (stateId) => {
  return axios.get("/cities", { params: { stateId } });
};
