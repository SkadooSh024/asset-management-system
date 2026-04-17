import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AppShell from "../components/layout/AppShell";
import ProtectedRoute from "../components/layout/ProtectedRoute";
import Login from "../pages/auth/Login";
import Dashboard from "../pages/dashboard/Dashboard";
import CatalogManagement from "../pages/catalog/CatalogManagement";
import AssetManagement from "../pages/assets/AssetManagement";
import AssignmentManagement from "../pages/assignments/AssignmentManagement";
import IncidentManagement from "../pages/incidents/IncidentManagement";
import MaintenanceManagement from "../pages/maintenance/MaintenanceManagement";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<AppShell />}>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route element={<ProtectedRoute roles={["ADMIN", "ASSET_STAFF", "MANAGER", "END_USER"]} />}>
              <Route path="/dashboard" element={<Dashboard />} />
            </Route>
            <Route element={<ProtectedRoute roles={["ADMIN", "ASSET_STAFF"]} />}>
              <Route path="/catalog" element={<CatalogManagement />} />
            </Route>
            <Route element={<ProtectedRoute roles={["ADMIN", "ASSET_STAFF", "MANAGER"]} />}>
              <Route path="/assets" element={<AssetManagement />} />
              <Route path="/assignments" element={<AssignmentManagement />} />
              <Route path="/maintenance" element={<MaintenanceManagement />} />
            </Route>
            <Route element={<ProtectedRoute roles={["ADMIN", "ASSET_STAFF", "MANAGER", "END_USER"]} />}>
              <Route path="/incidents" element={<IncidentManagement />} />
            </Route>
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
