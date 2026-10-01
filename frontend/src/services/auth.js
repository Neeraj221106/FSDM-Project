// Backend API configuration
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

// JWT token management
export const getToken = () => localStorage.getItem("jwt_token");
export const setToken = (token) => localStorage.setItem("jwt_token", token);
export const removeToken = () => localStorage.removeItem("jwt_token");

// User data management
export const getUserData = () => {
  const userData = localStorage.getItem("user_data");
  return userData ? JSON.parse(userData) : null;
};
export const setUserData = (data) => localStorage.setItem("user_data", JSON.stringify(data));
export const removeUserData = () => localStorage.removeItem("user_data");