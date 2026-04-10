import { useState } from "react";
import axiosClient from "../../api/axiosClient";

function Login() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [message, setMessage] = useState("");

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

    try {
      const response = await axiosClient.post("/api/auth/login", formData);
      if (response.data.success) {
        setMessage(`Xin chào ${response.data.user.fullName}`);
      } else {
        setMessage(response.data.message);
      }
    } catch (error) {
      setMessage("Không kết nối được server");
      console.error(error);
    }
  };

  return (
    <div className="container-fluid min-vh-100 d-flex align-items-center justify-content-center bg-light">
      <div className="card shadow p-4" style={{ width: "100%", maxWidth: "420px" }}>
        <h2 className="text-center mb-3 text-primary">Đăng nhập</h2>
        <p className="text-center text-muted">Hệ thống quản lý tài sản</p>

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Tên đăng nhập</label>
            <input
              type="text"
              className="form-control"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Nhập username"
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Mật khẩu</label>
            <input
              type="password"
              className="form-control"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Nhập password"
            />
          </div>

          <button type="submit" className="btn btn-primary w-100">
            Đăng nhập
          </button>

          {message && <div className="alert alert-info mt-3 mb-0">{message}</div>}
        </form>
      </div>
    </div>
  );
}

export default Login;