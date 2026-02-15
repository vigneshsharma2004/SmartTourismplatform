import axios from "./axiosConfig";

export const getStates = () => {
  return axios.get("/states");
};
