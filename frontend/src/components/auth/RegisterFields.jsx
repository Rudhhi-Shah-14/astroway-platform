import { Mail, User } from "lucide-react";

import Input from "../common/Input";
import RoleSelector from "./RoleSelector";

const RegisterFields = ({
  formData,
  onChange,
  disabled = false,
}) => {
  return (
    <>
      {/* Username */}
      <div className="relative">
        <User
          size={18}
          strokeWidth={2}
          className="pointer-events-none absolute left-4 top-1/2 z-10 -translate-y-1/2 text-gray-500"
        />

        <Input
          name="username"
          type="text"
          value={formData.username}
          onChange={onChange}
          placeholder="Username"
          required
          autoComplete="username"
          disabled={disabled}
          className="pl-12"
        />
      </div>

      {/* Email */}
      <div className="relative">
        <Mail
          size={18}
          strokeWidth={2}
          className="pointer-events-none absolute left-4 top-1/2 z-10 -translate-y-1/2 text-gray-500"
        />

        <Input
          name="email"
          type="email"
          value={formData.email}
          onChange={onChange}
          placeholder="Email"
          required
          autoComplete="email"
          disabled={disabled}
          className="pl-12"
        />
      </div>

      {/* Role */}
      <RoleSelector
        name="role"
        value={formData.role}
        onChange={onChange}
        required
        disabled={disabled}
      />
    </>
  );
};

export default RegisterFields;