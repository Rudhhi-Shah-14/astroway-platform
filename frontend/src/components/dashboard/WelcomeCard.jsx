import { LogOut, ShieldCheck, User } from "lucide-react";

const WelcomeCard = ({ user, onLogout }) => {
  return (
    <div className="w-full max-w-md rounded-2xl border border-white/10 bg-[#151B27] p-8 text-center shadow-2xl">
      {/* User icon */}
      <div className="mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-full border border-cyan-400/30 bg-cyan-400/10 text-cyan-400">
        <User size={30} />
      </div>

      {/* Welcome */}
      <h2 className="text-2xl font-bold text-white">
        Welcome, {user.username}!
      </h2>

      <p className="mt-2 text-sm text-gray-400">
        Authenticated via Spring Security & JWT
      </p>

      {/* User information */}
      <div className="mt-6 space-y-3 rounded-xl border border-white/10 bg-[#0B0F19] p-4 text-left text-xs">
        <div className="flex items-center justify-between gap-4">
          <span className="text-gray-400">
            User ID:
          </span>

          <span className="break-all font-mono text-cyan-400">
            {user.userId}
          </span>
        </div>

        <div className="flex items-center justify-between gap-4">
          <span className="text-gray-400">
            Roles:
          </span>

          <span className="flex items-center gap-1 text-right font-semibold text-cyan-400">
            <ShieldCheck size={14} />
            {user.roles?.join(", ")}
          </span>
        </div>
      </div>

      {/* Logout */}
      <button
        type="button"
        onClick={onLogout}
        className="mt-6 flex w-full items-center justify-center gap-2 rounded-lg bg-white/5 px-4 py-3 text-sm font-medium text-gray-300 transition hover:bg-white/10 hover:text-white"
      >
        <LogOut size={16} />
        Sign Out
      </button>
    </div>
  );
};

export default WelcomeCard;