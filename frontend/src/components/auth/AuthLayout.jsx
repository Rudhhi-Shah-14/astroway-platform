const AuthLayout = ({ header, children }) => {
  return (
    <main className="min-h-screen bg-[#0B0F19] px-4 py-12 text-gray-100">
      <div className="mx-auto flex min-h-[calc(100vh-6rem)] w-full max-w-md flex-col justify-center">
        {header}

        <section className="rounded-2xl border border-white/10 bg-[#151B27] p-6 shadow-2xl">
          {children}
        </section>
      </div>
    </main>
  );
};

export default AuthLayout;