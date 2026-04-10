import { useState } from "react";

function Login() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert(
      `Đăng nhập thử:\nTên đăng nhập: ${formData.username}\nMật khẩu: ${formData.password}`
    );
  };

  return (
    <div className="login-page d-flex align-items-center justify-content-center">
      <div className="card shadow-lg border-0 login-card">
        <div className="card-body p-4 p-md-5">
          <div className="text-center mb-4">
            <h2 className="fw-bold text-primary mb-2">Đăng nhập</h2>
            <p className="text-muted mb-0">Hệ thống quản lý tài sản</p>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="username" className="form-label fw-semibold">
                Tên đăng nhập
              </label>
              <input
                type="text"
                className="form-control"
                id="username"
                name="username"
                placeholder="Nhập tên đăng nhập"
                value={formData.username}
                onChange={handleChange}
              />
            </div>

            <div className="mb-3">
              <label htmlFor="password" className="form-label fw-semibold">
                Mật khẩu
              </label>
              <input
                type="password"
                className="form-control"
                id="password"
                name="password"
                placeholder="Nhập mật khẩu"
                value={formData.password}
                onChange={handleChange}
              />
            </div>

            <div className="d-grid mt-4">
              <button type="submit" className="btn btn-primary btn-lg">
                Đăng nhập
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;