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
import { MODULE_ACCESS } from "../config/roleAccess";

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<AppShell />}>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route element={<ProtectedRoute roles={MODULE_ACCESS.DASHBOARD} />}>
              <Route path="/dashboard" element={<Dashboard />} />
            </Route>
            <Route element={<ProtectedRoute roles={MODULE_ACCESS.CATALOG} />}>
              <Route path="/catalog" element={<CatalogManagement />} />
            </Route>
            <Route element={<ProtectedRoute roles={MODULE_ACCESS.ASSET} />}>
              <Route path="/assets" element={<AssetManagement />} />
            </Route>
            <Route element={<ProtectedRoute roles={MODULE_ACCESS.ASSIGNMENT} />}>
              <Route path="/assignments" element={<AssignmentManagement />} />
            </Route>
            <Route element={<ProtectedRoute roles={MODULE_ACCESS.MAINTENANCE} />}>
              <Route path="/maintenance" element={<MaintenanceManagement />} />
            </Route>
            <Route element={<ProtectedRoute roles={MODULE_ACCESS.INCIDENT} />}>
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
