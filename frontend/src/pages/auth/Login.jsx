import { useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import axiosClient from "../../api/axiosClient";
import { setStoredUser, getStoredUser } from "../../utils/auth";
import { getApiErrorMessage } from "../../utils/format";

const INITIAL_FORM = {
  username: "",
  password: "",
};

function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const storedUser = getStoredUser();
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  if (storedUser) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const { data } = await axiosClient.post("/api/auth/login", formData);
      setStoredUser(data.user);
      navigate(location.state?.from || "/dashboard", { replace: true });
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Đăng nhập thất bại."));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-screen">
      <div className="login-screen__content">
        <div className="login-intro">
          <span className="login-tag">Thực tập tốt nghiệp</span>
          <h1>Hệ thống quản lý tài sản, cấp phát và bảo trì thiết bị</h1>
          <p>
            Đăng nhập để thao tác trên 6 module lõi: danh mục, tài sản, cấp phát,
            báo hỏng, bảo trì và tổng quan hệ thống.
          </p>
          <div className="login-highlights">
            <div>
              <strong>Track</strong>
              <span>Vòng đời tài sản</span>
            </div>
            <div>
              <strong>Operate</strong>
              <span>Nghiệp vụ rõ ràng</span>
            </div>
            <div>
              <strong>Audit</strong>
              <span>Lịch sử và truy vết</span>
            </div>
          </div>
        </div>

        <div className="login-card">
          <div className="login-card__header">
            <h2>Đăng nhập hệ thống</h2>
            <p>Sử dụng tài khoản demo trong dữ liệu mẫu để thao tác.</p>
          </div>

          <form onSubmit={handleSubmit} className="login-form">
            <div>
              <label htmlFor="username" className="form-label">
                Tên đăng nhập
              </label>
              <input
                id="username"
                type="text"
                name="username"
                className="form-control"
                value={formData.username}
                onChange={handleChange}
                placeholder="Nhập tên đăng nhập"
                required
              />
            </div>

            <div>
              <label htmlFor="password" className="form-label">
                Mật khẩu
              </label>
              <input
                id="password"
                type="password"
                name="password"
                className="form-control"
                value={formData.password}
                onChange={handleChange}
                placeholder="Nhập mật khẩu"
                required
              />
            </div>

            {error ? <div className="alert alert-danger mb-0">{error}</div> : null}

            <button type="submit" className="btn btn-primary btn-lg w-100" disabled={loading}>
              {loading ? "Đang xác thực..." : "Đăng nhập"}
            </button>
          </form>

          <div className="login-note">
            <small>
              Gợi ý demo: `admin / admin123`, `asset_admin / manager123`,
              `training_user / staff123`
            </small>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
