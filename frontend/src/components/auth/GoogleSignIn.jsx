import { GoogleLogin } from "@react-oauth/google";

const GoogleSignIn = ({
  onSuccess,
  onError,
  disabled = false,
}) => {
  return (
    <div className={disabled ? "pointer-events-none opacity-50" : ""}>
      <GoogleLogin
        onSuccess={onSuccess}
        onError={onError}
        theme="filled_dark"
        shape="circle"
      />
    </div>
  );
};

export default GoogleSignIn;