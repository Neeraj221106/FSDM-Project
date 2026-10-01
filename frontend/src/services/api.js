import axios from "axios";
import { getToken, removeToken, removeUserData } from "./auth";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: { "Content-Type": "application/json" },
});

// Attach Spring Boot JWT to every request
api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 401 → clear JWT and redirect
api.interceptors.response.use(
  (res) => res,
  async (err) => {
    if (err.response?.status === 401) {
      removeToken();
      removeUserData();
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

export default api;