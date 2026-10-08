import React from "react";

import useAuth from "../hooks/useAuth";

import AuthError from "./auth/AuthError";
import AuthHeader from "./auth/AuthHeader";
import AuthLayout from "./auth/AuthLayout";
import AuthTabs from "./auth/AuthTabs";
import LoginFields from "./auth/LoginFields";
import RegisterFields from "./auth/RegisterFields";

import PasswordField from "./common/PasswordField";

const AuthForm = ({ onAuthSuccess }) => {
  const {
    isLogin,
    showPassword,
    loading,
    errorMessage,
    formData,
    handleChange,
    handleModeChange,
    togglePasswordVisibility,
    handleSubmit,
  } = useAuth({
    onAuthSuccess,
  });

  return (
    <AuthLayout header={<AuthHeader />}>
      <AuthTabs
        isLogin={isLogin}
        onModeChange={handleModeChange}
      />

      <AuthError message={errorMessage} />

      <form onSubmit={handleSubmit} className="space-y-4">
        {isLogin ? (
          <LoginFields
            formData={formData}
            onChange={handleChange}
            disabled={loading}
          />
        ) : (
          <RegisterFields
            formData={formData}
            onChange={handleChange}
            disabled={loading}
          />
        )}

        <PasswordField
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Password"
          showPassword={showPassword}
          onTogglePassword={togglePasswordVisibility}
          required
          autoComplete={
            isLogin ? "current-password" : "new-password"
          }
          disabled={loading}
        />

        <button
          type="submit"
          disabled={loading}
          className="mt-2 w-full rounded-lg bg-cyan-400 px-4 py-3 text-sm font-semibold text-[#0B0F19] transition hover:bg-cyan-300 focus:outline-none focus:ring-2 focus:ring-cyan-400/50 disabled:cursor-not-allowed disabled:opacity-50"
        >
          {loading
            ? "Please wait..."
            : isLogin
              ? "Login"
              : "Create Account"}
        </button>
      </form>
    </AuthLayout>
  );
};

export default AuthForm;