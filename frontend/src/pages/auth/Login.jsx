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
      setError(getApiErrorMessage(requestError, "Dang nhap that bai."));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-screen">
      <div className="login-screen__content">
        <div className="login-intro">
          <span className="login-tag">Thuc tap tot nghiep</span>
          <h1>He thong quan ly tai san, cap phat va bao tri thiet bi</h1>
          <p>
            Dang nhap de thao tac tren 6 module loi: danh muc, tai san, cap phat,
            bao hong, bao tri va tong quan he thong.
          </p>
          <div className="login-highlights">
            <div>
              <strong>Track</strong>
              <span>Vong doi tai san</span>
            </div>
            <div>
              <strong>Operate</strong>
              <span>Nghiep vu ro rang</span>
            </div>
            <div>
              <strong>Audit</strong>
              <span>Lich su va truy vet</span>
            </div>
          </div>
        </div>

        <div className="login-card">
          <div className="login-card__header">
            <h2>Dang nhap he thong</h2>
            <p>Su dung tai khoan demo trong database seed de thao tac.</p>
          </div>

          <form onSubmit={handleSubmit} className="login-form">
            <div>
              <label htmlFor="username" className="form-label">
                Ten dang nhap
              </label>
              <input
                id="username"
                type="text"
                name="username"
                className="form-control"
                value={formData.username}
                onChange={handleChange}
                placeholder="Nhap ten dang nhap"
                required
              />
            </div>

            <div>
              <label htmlFor="password" className="form-label">
                Mat khau
              </label>
              <input
                id="password"
                type="password"
                name="password"
                className="form-control"
                value={formData.password}
                onChange={handleChange}
                placeholder="Nhap mat khau"
                required
              />
            </div>

            {error ? <div className="alert alert-danger mb-0">{error}</div> : null}

            <button type="submit" className="btn btn-primary btn-lg w-100" disabled={loading}>
              {loading ? "Dang xac thuc..." : "Dang nhap"}
            </button>
          </form>

          <div className="login-note">
            <small>
              Goi y demo: `admin / admin123`, `asset_admin / manager123`,
              `training_user / staff123`
            </small>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
