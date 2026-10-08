import { useEffect, useState } from "react";

import AuthForm from "./components/AuthForm";
import WelcomeCard from "./components/dashboard/WelcomeCard";

import { authStorage } from "./utils/authStorage";

const App = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const savedUser = authStorage.getUser();

    if (savedUser) {
      setUser(savedUser);
    }
  }, []);

  const handleAuthSuccess = (data) => {
    setUser({
      username: data.username,
      roles: data.roles,
      userId: data.userId,
    });
  };

  const handleLogout = () => {
    authStorage.clear();
    setUser(null);
  };

  if (!user) {
    return <AuthForm onAuthSuccess={handleAuthSuccess} />;
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-[#0B0F19] px-4 py-12 text-gray-100">
      <WelcomeCard
        user={user}
        onLogout={handleLogout}
      />
    </main>
  );
};

export default App;