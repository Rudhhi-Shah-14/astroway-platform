const AUTH_STORAGE_KEYS = {
  ACCESS_TOKEN: 'accessToken',
  REFRESH_TOKEN: 'refreshToken',
  USER: 'user',
};

export const authStorage = {
  setAuthData(data) {
    localStorage.setItem(AUTH_STORAGE_KEYS.ACCESS_TOKEN, data.accessToken);
    localStorage.setItem(AUTH_STORAGE_KEYS.REFRESH_TOKEN, data.refreshToken);

    localStorage.setItem(
      AUTH_STORAGE_KEYS.USER,
      JSON.stringify({
        username: data.username,
        roles: data.roles,
        userId: data.userId,
      })
    );
  },

  getAccessToken() {
    return localStorage.getItem(AUTH_STORAGE_KEYS.ACCESS_TOKEN);
  },

  getRefreshToken() {
    return localStorage.getItem(AUTH_STORAGE_KEYS.REFRESH_TOKEN);
  },

  getUser() {
    const user = localStorage.getItem(AUTH_STORAGE_KEYS.USER);

    return user ? JSON.parse(user) : null;
  },

  clear() {
    localStorage.removeItem(AUTH_STORAGE_KEYS.ACCESS_TOKEN);
    localStorage.removeItem(AUTH_STORAGE_KEYS.REFRESH_TOKEN);
    localStorage.removeItem(AUTH_STORAGE_KEYS.USER);
  },
};