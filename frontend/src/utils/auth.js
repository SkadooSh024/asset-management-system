const AUTH_STORAGE_KEY = "asset_management_user";

export function getStoredUser() {
  const raw = localStorage.getItem(AUTH_STORAGE_KEY);

  if (!raw) {
    return null;
  }

  try {
    return JSON.parse(raw);
  } catch {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    return null;
  }
}

export function setStoredUser(user) {
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(user));
}

export function clearStoredUser() {
  localStorage.removeItem(AUTH_STORAGE_KEY);
}

export function hasAnyRole(user, roleCodes = []) {
  return Boolean(user?.roleCode && roleCodes.includes(user.roleCode));
}
