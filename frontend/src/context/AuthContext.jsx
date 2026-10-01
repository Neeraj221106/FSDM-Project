import { createContext, useEffect, useState } from "react";
import {
  getToken,
  setToken,
  removeToken,
  getUserData,
  setUserData,
  removeUserData,
  API_BASE_URL,
} from "../services/auth";

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);
  const [loading, setLoading] = useState(true);

  // Restore login session when app starts
  useEffect(() => {
    const token = getToken();
    const userData = getUserData();

    if (token && userData) {
      setUser(userData);
      setRole(userData.role);
    }

    setLoading(false);
  }, []);

  // Login
  const signIn = async (email, password) => {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email,
        password,
      }),
    });

    // Handle login error
    if (!response.ok) {
      let errorMessage = "Login failed";

      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch {
        // Ignore invalid JSON response
      }

      throw new Error(errorMessage);
    }

    const data = await response.json();

    // Make sure backend returned required data
    if (!data.token) {
      throw new Error("Login successful but no token was returned.");
    }

    if (!data.email || !data.role) {
      throw new Error("Login successful but user role was not returned.");
    }

    // Create complete user object
    const userData = {
      email: data.email,
      role: data.role,
    };

    // Save JWT
    setToken(data.token);

    // Save user data in localStorage
    setUserData(userData);

    // Update React state
    setUser(userData);
    setRole(data.role);

    // Return complete user object
    return userData;
  };

  // Logout
  const signOut = async () => {
    removeToken();
    removeUserData();

    setUser(null);
    setRole(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        role,
        loading,
        signIn,
        signOut,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}