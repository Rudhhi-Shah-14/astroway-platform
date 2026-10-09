import React from "react";
import { User } from "lucide-react";

import useAuth from "../hooks/useAuth";

import AuthError from "./auth/AuthError";
import AuthHeader from "./auth/AuthHeader";
import AuthLayout from "./auth/AuthLayout";
import AuthTabs from "./auth/AuthTabs";
import GoogleSignIn from "./auth/GoogleSignIn";
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
    handleGoogleLogin,
    handleGoogleError,
    handleGuestLogin,
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

      {/* Google Sign-In */}
      <div className="my-6 flex items-center gap-3">
        <div className="h-px flex-1 bg-white/10" />

        <span className="text-xs text-gray-500">
          OR
        </span>

        <div className="h-px flex-1 bg-white/10" />
      </div>

      <div className="flex justify-center">
        <GoogleSignIn
          onSuccess={handleGoogleLogin}
          onError={handleGoogleError}
          disabled={loading}
        />
      </div>

      {/* Guest Login */}
      <div className="relative my-6">
        <div className="absolute inset-0 flex items-center">
          <div className="w-full border-t border-white/10" />
        </div>

        <div className="relative flex justify-center text-xs uppercase">
          <span className="bg-[#0B0F19] px-2 text-gray-500">
            OR
          </span>
        </div>
      </div>

      <button
        type="button"
        onClick={handleGuestLogin}
        disabled={loading}
        className="w-full rounded-lg border border-white/10 bg-white/5 px-4 py-3 text-sm font-medium text-gray-300 transition hover:bg-white/10 focus:outline-none focus:ring-2 focus:ring-cyan-400/50 disabled:cursor-not-allowed disabled:opacity-50"
      >
        <span className="flex items-center justify-center gap-2">
          <User className="h-4 w-4 text-gray-400" />
          Continue as Guest Stargazer
        </span>
      </button>
    </AuthLayout>
  );
};

export default AuthForm;