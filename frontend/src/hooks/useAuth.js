import { useState } from "react";

import { authAPI } from "../services/api";
import { authStorage } from "../utils/authStorage";

const INITIAL_FORM_DATA = {
  username: "",
  email: "",
  password: "",
  usernameOrEmail: "",
  role: "ROLE_EXPLORER",
};

const useAuth = ({ onAuthSuccess }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [formData, setFormData] = useState(INITIAL_FORM_DATA);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));

    setErrorMessage("");
  };

  const handleModeChange = (loginMode) => {
    setIsLogin(loginMode);
    setErrorMessage("");
  };

  const togglePasswordVisibility = () => {
    setShowPassword((previous) => !previous);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setLoading(true);
    setErrorMessage("");

    try {
      let response;

      if (isLogin) {
        response = await authAPI.login({
          usernameOrEmail: formData.usernameOrEmail,
          password: formData.password,
        });
      } else {
        response = await authAPI.register({
          username: formData.username,
          email: formData.email,
          password: formData.password,
          role: formData.role,
        });
      }

      authStorage.setAuthData(response.data);

      onAuthSuccess(response.data);
    } catch (err) {
      console.error("Authentication error:", err);

      if (err.response?.data?.validationErrors) {
        const validationErrors = err.response.data.validationErrors;
        const firstError = Object.values(validationErrors)[0];

        setErrorMessage(firstError);
      } else if (err.response?.data?.message) {
        setErrorMessage(err.response.data.message);
      } else if (!err.response) {
        setErrorMessage(
          "Cannot connect to Auth Service on localhost:8081."
        );
      } else {
        setErrorMessage(
          "Authentication failed. Please check your credentials."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  // Google Sign-In
  const handleGoogleLogin = async (credentialResponse) => {
    if (!credentialResponse?.credential) {
      setErrorMessage("Google Sign-In failed.");
      return;
    }

    setLoading(true);
    setErrorMessage("");

    try {
      const response = await authAPI.googleLogin({
        idToken: credentialResponse.credential,
        role: formData.role,
      });

      authStorage.setAuthData(response.data);

      onAuthSuccess(response.data);
    } catch (err) {
      console.error("Google authentication error:", err);

      if (err.response?.data?.validationErrors) {
        const validationErrors = err.response.data.validationErrors;
        const firstError = Object.values(validationErrors)[0];

        setErrorMessage(firstError);
      } else if (err.response?.data?.message) {
        setErrorMessage(err.response.data.message);
      } else if (!err.response) {
        setErrorMessage(
          "Cannot connect to Auth Service on localhost:8081."
        );
      } else {
        setErrorMessage("Google Sign-In failed.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleError = () => {
    setErrorMessage("Google Sign-In failed.");
  };

  return {
    isLogin,
    showPassword,
    loading,
    errorMessage,
    formData,

    handleChange,
    handleModeChange,
    togglePasswordVisibility,
    handleSubmit,

    handleGoogleLogin,
    handleGoogleError,
  };
};

export default useAuth;