import { useState } from "react";
import { NavLink, Outlet, useLocation, useNavigate } from "react-router-dom";
import axiosClient from "../../api/axiosClient";
import { useToast } from "../common/ToastProvider";
import { clearStoredUser, getStoredUser, hasAnyRole } from "../../utils/auth";
import { MODULE_ACCESS } from "../../config/roleAccess";

const MENU_ITEMS = [
  { to: "/dashboard", label: "Tổng quan", roles: MODULE_ACCESS.DASHBOARD },
  { to: "/catalog", label: "Danh mục", roles: MODULE_ACCESS.CATALOG },
  { to: "/assets", label: "Tài sản", roles: MODULE_ACCESS.ASSET },
  { to: "/assignments", label: "Cấp phát", roles: MODULE_ACCESS.ASSIGNMENT },
  { to: "/incidents", label: "Báo hỏng", roles: MODULE_ACCESS.INCIDENT },
  { to: "/maintenance", label: "Bảo trì", roles: MODULE_ACCESS.MAINTENANCE },
];

const PAGE_TITLES = {
  "/dashboard": "Tổng quan hệ thống",
  "/catalog": "Danh mục tài sản",
  "/assets": "Quản lý tài sản",
  "/assignments": "Cấp phát tài sản",
  "/incidents": "Báo hỏng và hỗ trợ",
  "/maintenance": "Quản lý bảo trì",
};

function AppShell() {
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const user = getStoredUser();
  const toast = useToast();

  const visibleItems = MENU_ITEMS.filter((item) => hasAnyRole(user, item.roles));

  const handleLogout = async () => {
    try {
      if (user?.username) {
        await axiosClient.post("/api/auth/logout", { username: user.username });
      }
    } catch {
      // Frontend đang dùng local session; logout vẫn được thực hiện ở client ngay cả khi API lỗi.
    } finally {
      clearStoredUser();
      toast.info("Đã đăng xuất khỏi hệ thống.");
      navigate("/login", { replace: true });
    }
  };

  return (
    <div className="app-shell">
      {sidebarOpen ? (
        <button type="button" className="sidebar-backdrop" onClick={() => setSidebarOpen(false)} aria-label="Đóng menu" />
      ) : null}

      <aside className={`app-sidebar ${sidebarOpen ? "is-open" : ""}`}>
        <div className="sidebar-brand">
          <div className="brand-mark">AM</div>
          <div>
            <h1>Asset System</h1>
            <p>Quản lý tài sản tập trung</p>
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
          <small>{user?.departmentName || "Chưa gắn phòng ban"}</small>
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
              Đăng xuất
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
