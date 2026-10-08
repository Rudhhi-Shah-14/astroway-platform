const Input = ({
  name,
  type = "text",
  value,
  onChange,
  placeholder,
  required = false,
  autoComplete,
  disabled = false,
  className = "",
}) => {
  return (
    <input
      name={name}
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      required={required}
      autoComplete={autoComplete}
      disabled={disabled}
      className={`w-full rounded-lg border border-white/10 bg-white/5 px-4 py-3 text-sm text-white placeholder-gray-500 outline-none transition focus:border-cyan-400 focus:ring-1 focus:ring-cyan-400/30 disabled:cursor-not-allowed disabled:opacity-50 ${className}`}
    />
  );
};

export default Input;