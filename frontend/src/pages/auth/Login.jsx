import { useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import axiosClient from "../../api/axiosClient";
import { useToast } from "../../components/common/ToastProvider";
import useFeedbackToast from "../../hooks/useFeedbackToast";
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
  const toast = useToast();
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useFeedbackToast({ errorMessage: error });

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
      toast.success("Đăng nhập thành công.");
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
          <h1>Hệ thống quản lý tài sản, cấp phát và bảo trì thiết bị</h1>
          
          <div className="login-highlights">
            <div>
              <strong>Quản lý</strong>
              <span>Vòng đời tài sản</span>
            </div>
            <div>
              <strong>Vận hành</strong>
              <span>Nghiệp vụ rõ ràng</span>
            </div>
            <div>
              <strong>Kiểm tra</strong>
              <span>Lịch sử và truy vết</span>
            </div>
          </div>
        </div>

        <div className="login-card">
          <div className="login-card__header">
            <h2>Đăng nhập hệ thống</h2>
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
              Demo: admin / admin123, asset_admin / manager123, training_user / staff123, lab_manager / staff123
            </small>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
