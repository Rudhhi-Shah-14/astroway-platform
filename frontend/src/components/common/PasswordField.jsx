import { Eye, EyeOff } from "lucide-react";

const PasswordField = ({
  name,
  value,
  onChange,
  placeholder = "Password",
  showPassword,
  onTogglePassword,
  required = false,
  autoComplete,
  disabled = false,
}) => {
  return (
    <div className="relative">
      <input
        name={name}
        type={showPassword ? "text" : "password"}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        autoComplete={autoComplete}
        disabled={disabled}
        className="w-full rounded-lg border border-white/10 bg-white/5 px-4 py-3 pr-12 text-sm text-white placeholder-gray-500 outline-none transition focus:border-cyan-400 focus:ring-1 focus:ring-cyan-400/30 disabled:cursor-not-allowed disabled:opacity-50"
      />

      <button
        type="button"
        onClick={onTogglePassword}
        disabled={disabled}
        aria-label={showPassword ? "Hide password" : "Show password"}
        className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500 transition hover:text-gray-200 disabled:cursor-not-allowed disabled:opacity-50"
      >
        {showPassword ? (
          <EyeOff size={18} strokeWidth={2} />
        ) : (
          <Eye size={18} strokeWidth={2} />
        )}
      </button>
    </div>
  );
};

export default PasswordField;