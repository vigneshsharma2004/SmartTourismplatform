import axios from "./axiosConfig";
import { jwtDecode } from "jwt-decode";

/* ================= AUTH APIs ================= */

export const login = (data) => {
  return axios.post("/api/auth/login", data);
};

export const signup = (data) => {
  return axios.post("/api/auth/signup", data);
};

/* ================= TOKEN HELPERS ================= */

export const saveToken = (token) => {
  localStorage.setItem("token", token);
};

export const logout = () => {
  localStorage.removeItem("token");
};

/* ================= AUTH CHECK ================= */

export const isAuthenticated = () => {
  const token = localStorage.getItem("token");
  if (!token) return false;

  try {
    const decoded = jwtDecode(token);

    // 🔑 Handle exp in milliseconds or seconds safely
    const expInSeconds =
      decoded.exp > 1e12 ? decoded.exp / 1000 : decoded.exp;

    if (expInSeconds < Date.now() / 1000) {
      logout();
      return false;
    }

    return true;
  } catch (err) {
    logout();
    return false;
  }
};
