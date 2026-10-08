const ROLES = [
  {
    value: "ROLE_EXPLORER",
    label: "Explorer",
  },
  {
    value: "ROLE_HOST",
    label: "Host",
  },
];

const RoleSelector = ({
  name = "role",
  value,
  onChange,
  required = false,
}) => {
  return (
    <select
      name={name}
      value={value}
      onChange={onChange}
      required={required}
      className="w-full rounded-lg border border-white/10 bg-white/5 px-4 py-3 text-sm text-white outline-none transition focus:border-cyan-400 focus:ring-1 focus:ring-cyan-400/30"
    >
      {ROLES.map((role) => (
        <option
          key={role.value}
          value={role.value}
          className="bg-[#151B27] text-white"
        >
          {role.label}
        </option>
      ))}
    </select>
  );
};

export default RoleSelector;