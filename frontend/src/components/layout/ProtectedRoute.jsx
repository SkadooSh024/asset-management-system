import { Navigate, Outlet, useLocation } from "react-router-dom";
import { getStoredUser, hasAnyRole } from "../../utils/auth";

function ProtectedRoute({ roles }) {
  const location = useLocation();
  const user = getStoredUser();

  if (!user) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (roles?.length && !hasAnyRole(user, roles)) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
}

export default ProtectedRoute;
