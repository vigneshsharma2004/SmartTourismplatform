import axios from "axios";
import { logout } from "./authApi";

const instance = axios.create({
  baseURL: "http://localhost:7778",
});

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (
      error.response &&
      (error.response.status === 401 || error.response.status === 403)
    ) {
      logout();
      // ❌ DO NOT redirect here
      // Let React Router handle navigation
    }
    return Promise.reject(error);
  }
);

export default instance;
