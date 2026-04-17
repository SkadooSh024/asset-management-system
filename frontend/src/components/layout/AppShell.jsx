import { useState } from "react";
import { NavLink, Outlet, useLocation, useNavigate } from "react-router-dom";
import axiosClient from "../../api/axiosClient";
import { clearStoredUser, getStoredUser, hasAnyRole } from "../../utils/auth";

const MENU_ITEMS = [
  { to: "/dashboard", label: "Tong quan", roles: ["ADMIN", "ASSET_STAFF", "MANAGER", "END_USER"] },
  { to: "/catalog", label: "Danh muc", roles: ["ADMIN", "ASSET_STAFF"] },
  { to: "/assets", label: "Tai san", roles: ["ADMIN", "ASSET_STAFF", "MANAGER"] },
  { to: "/assignments", label: "Cap phat", roles: ["ADMIN", "ASSET_STAFF", "MANAGER"] },
  { to: "/incidents", label: "Bao hong", roles: ["ADMIN", "ASSET_STAFF", "MANAGER", "END_USER"] },
  { to: "/maintenance", label: "Bao tri", roles: ["ADMIN", "ASSET_STAFF", "MANAGER"] },
];

const PAGE_TITLES = {
  "/dashboard": "Tong quan he thong",
  "/catalog": "Danh muc tai san",
  "/assets": "Quan ly tai san",
  "/assignments": "Cap phat tai san",
  "/incidents": "Bao hong va ho tro",
  "/maintenance": "Quan ly bao tri",
};

function AppShell() {
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const user = getStoredUser();

  const visibleItems = MENU_ITEMS.filter((item) => hasAnyRole(user, item.roles));

  const handleLogout = async () => {
    try {
      if (user?.username) {
        await axiosClient.post("/api/auth/logout", { username: user.username });
      }
    } catch {
      // Frontend dang dung local session; logout van duoc thuc hien o client ngay ca khi API loi.
    } finally {
      clearStoredUser();
      navigate("/login", { replace: true });
    }
  };

  return (
    <div className="app-shell">
      {sidebarOpen ? (
        <button type="button" className="sidebar-backdrop" onClick={() => setSidebarOpen(false)} aria-label="Dong menu" />
      ) : null}

      <aside className={`app-sidebar ${sidebarOpen ? "is-open" : ""}`}>
        <div className="sidebar-brand">
          <div className="brand-mark">AM</div>
          <div>
            <h1>Asset System</h1>
            <p>Quan ly tai san tap trung</p>
          </div>
        </div>

        <nav className="sidebar-nav">
          {visibleItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `sidebar-link ${isActive ? "is-active" : ""}`
              }
              onClick={() => setSidebarOpen(false)}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-user">
          <span className="role-chip">{user?.roleName || user?.roleCode}</span>
          <strong>{user?.fullName}</strong>
          <small>{user?.departmentName || "Chua gan phong ban"}</small>
        </div>
      </aside>

      <div className="app-main">
        <header className="app-topbar">
          <button
            type="button"
            className="sidebar-toggle"
            onClick={() => setSidebarOpen((current) => !current)}
          >
            Menu
          </button>

          <div className="topbar-context">
            <span className="topbar-path">{location.pathname.replace("/", "") || "dashboard"}</span>
            <h2>{PAGE_TITLES[location.pathname] || "Asset Management"}</h2>
          </div>

          <div className="topbar-actions">
            <div className="user-compact">
              <strong>{user?.username}</strong>
              <span>{user?.email}</span>
            </div>
            <button type="button" className="btn btn-outline-primary" onClick={handleLogout}>
              Dang xuat
            </button>
          </div>
        </header>

        <main className="app-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default AppShell;
