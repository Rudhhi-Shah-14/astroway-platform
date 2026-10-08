import { Compass } from "lucide-react";

const AuthHeader = ({ isLogin }) => {
  return (
    <header className="mb-8 text-center">
      <div className="mb-5 flex justify-center">
        <div className="flex h-14 w-14 items-center justify-center rounded-full border-2 border-cyan-400 bg-cyan-400/10">
          <Compass
            size={30}
            strokeWidth={2}
            className="text-cyan-400"
          />
        </div>
      </div>

      <h1 className="text-3xl font-bold tracking-tight text-white">
        AstroWay
      </h1>

      <p className="mt-2 text-base text-gray-400">
        Explore. Host. Connect.
      </p>
    </header>
  );
};

export default AuthHeader;