import { User } from "lucide-react";

import Input from "../common/Input";

const LoginFields = ({
  formData,
  onChange,
  disabled = false,
}) => {
  return (
    <div className="relative">
      <User
        size={18}
        strokeWidth={2}
        className="pointer-events-none absolute left-4 top-1/2 z-10 -translate-y-1/2 text-gray-500"
      />

      <Input
        name="usernameOrEmail"
        type="text"
        value={formData.usernameOrEmail}
        onChange={onChange}
        placeholder="Username or Email"
        required
        autoComplete="username"
        disabled={disabled}
        className="pl-12"
      />
    </div>
  );
};

export default LoginFields;