import { useMemo, useState } from "react";
import axiosClient from "../../api/axiosClient";

function Login() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [userInfo, setUserInfo] = useState(null);



  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setUserInfo(null);
    setLoading(true);

    try {
      const response = await axiosClient.post("/api/auth/login", formData);

      if (response.data?.success) {
        setMessage(response.data.message || "Đăng nhập thành công");
        setUserInfo(response.data.user || null);
      } else {
        setMessage(response.data?.message || "Đăng nhập thất bại");
      }
    } catch (error) {
      console.error("Login error:", error);

      if (error.response) {
        const serverMessage = error.response.data?.message;
        setMessage(
          serverMessage || `Lỗi server: ${error.response.status}`
        );
      } else if (error.request) {
        setMessage(
          "Không kết nối được tới backend. Kiểm tra lại URL API hoặc CORS ở backend."
        );
      } else {
        setMessage("Có lỗi xảy ra khi đăng nhập");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page bg-light min-vh-100 d-flex align-items-center">
      <div className="container py-4">
        <div className="row justify-content-center">
          <div className="col-12 col-sm-10 col-md-8 col-lg-5 col-xl-4">
            <div className="card shadow-lg border-0 login-card">
              <div className="card-body p-4 p-md-5">
                <div className="text-center mb-4">
                  <h2 className="fw-bold text-primary">Đăng nhập</h2>
                  <p className="text-muted mb-2">Hệ thống quản lý tài sản</p>
                </div>

                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label htmlFor="username" className="form-label fw-semibold">
                      Tên đăng nhập
                    </label>
                    <input
                      id="username"
                      type="text"
                      name="username"
                      className="form-control"
                      placeholder="Nhập tên đăng nhập"
                      value={formData.username}
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="mb-3">
                    <label htmlFor="password" className="form-label fw-semibold">
                      Mật khẩu
                    </label>
                    <input
                      id="password"
                      type="password"
                      name="password"
                      className="form-control"
                      placeholder="Nhập mật khẩu"
                      value={formData.password}
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="d-grid mt-4">
                    <button
                      type="submit"
                      className="btn btn-primary btn-lg"
                      disabled={loading}
                    >
                      {loading ? "Đang xử lý..." : "Đăng nhập"}
                    </button>
                  </div>
                </form>

                {message && (
                  <div
                    className={`alert mt-4 mb-0 ${
                      userInfo ? "alert-success" : "alert-warning"
                    }`}
                  >
                    {message}
                  </div>
                )}

                {userInfo && (
                  <div className="card mt-3 border-success">
                    <div className="card-body">
                      <h6 className="fw-bold text-success mb-3">
                        Thông tin người dùng
                      </h6>
                      <p className="mb-1">
                        <strong>User ID:</strong> {userInfo.userId}
                      </p>
                      <p className="mb-1">
                        <strong>Username:</strong> {userInfo.username}
                      </p>
                      <p className="mb-1">
                        <strong>Họ tên:</strong> {userInfo.fullName}
                      </p>
                      <p className="mb-1">
                        <strong>Email:</strong> {userInfo.email}
                      </p>
                      <p className="mb-0">
                        <strong>Trạng thái:</strong> {userInfo.status}
                      </p>
                    </div>
                  </div>
                )}

            
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
