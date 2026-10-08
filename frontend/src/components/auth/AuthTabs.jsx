const AuthTabs = ({ isLogin, onModeChange }) => {
  return (
    <div className="mb-6 flex rounded-xl bg-white/5 p-1">
      <button
        type="button"
        onClick={() => onModeChange(true)}
        className={`flex-1 rounded-lg px-4 py-2.5 text-sm font-semibold transition ${
          isLogin
            ? "bg-cyan-400 text-[#0B0F19] shadow-sm"
            : "text-gray-400 hover:text-white"
        }`}
      >
        Login
      </button>

      <button
        type="button"
        onClick={() => onModeChange(false)}
        className={`flex-1 rounded-lg px-4 py-2.5 text-sm font-semibold transition ${
          !isLogin
            ? "bg-cyan-400 text-[#0B0F19] shadow-sm"
            : "text-gray-400 hover:text-white"
        }`}
      >
        Register
      </button>
    </div>
  );
};

export default AuthTabs;