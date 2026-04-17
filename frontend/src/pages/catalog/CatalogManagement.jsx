import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import { getStoredUser } from "../../utils/auth";
import { getApiErrorMessage } from "../../utils/format";

const EMPTY_CATEGORY_FORM = {
  categoryCode: "",
  categoryName: "",
  parentCategoryId: "",
  defaultWarrantyMonths: "",
  defaultMaintenanceCycleDays: "",
  description: "",
  isActive: true,
};

const EMPTY_STATUS_FORM = {
  statusCode: "",
  statusName: "",
  statusGroup: "STORAGE",
  isAllocatable: false,
  sortOrder: 0,
  description: "",
};

const STATUS_GROUP_LABELS = {
  STORAGE: "Lưu kho",
  USAGE: "Sử dụng",
  MAINTENANCE: "Bảo trì",
  END_OF_LIFE: "Kết thúc vòng đời",
  EXCEPTION: "Ngoại lệ",
};

function CatalogManagement() {
  const user = getStoredUser();
  const [categories, setCategories] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [categoryForm, setCategoryForm] = useState(EMPTY_CATEGORY_FORM);
  const [statusForm, setStatusForm] = useState(EMPTY_STATUS_FORM);
  const [editingCategoryId, setEditingCategoryId] = useState(null);
  const [editingStatusId, setEditingStatusId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const canManage = ["ADMIN", "ASSET_STAFF"].includes(user?.roleCode);

  const fetchCatalog = async () => {
    setLoading(true);
    setError("");

    try {
      const [categoriesResponse, statusesResponse] = await Promise.all([
        axiosClient.get("/api/catalog/categories"),
        axiosClient.get("/api/catalog/statuses"),
      ]);

      setCategories(categoriesResponse.data);
      setStatuses(statusesResponse.data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được dữ liệu danh mục."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCatalog();
  }, []);

  const handleCategoryChange = (event) => {
    const { name, value, type, checked } = event.target;
    setCategoryForm((current) => ({
      ...current,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleStatusChange = (event) => {
    const { name, value, type, checked } = event.target;
    setStatusForm((current) => ({
      ...current,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const submitCategory = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      if (editingCategoryId) {
        await axiosClient.put(`/api/catalog/categories/${editingCategoryId}`, {
          ...categoryForm,
          parentCategoryId: categoryForm.parentCategoryId || null,
          defaultWarrantyMonths: categoryForm.defaultWarrantyMonths || null,
          defaultMaintenanceCycleDays: categoryForm.defaultMaintenanceCycleDays || null,
        });
        setMessage("Cập nhật danh mục tài sản thành công.");
      } else {
        await axiosClient.post("/api/catalog/categories", {
          ...categoryForm,
          parentCategoryId: categoryForm.parentCategoryId || null,
          defaultWarrantyMonths: categoryForm.defaultWarrantyMonths || null,
          defaultMaintenanceCycleDays: categoryForm.defaultMaintenanceCycleDays || null,
        });
        setMessage("Thêm danh mục tài sản thành công.");
      }

      setCategoryForm(EMPTY_CATEGORY_FORM);
      setEditingCategoryId(null);
      fetchCatalog();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không lưu được danh mục tài sản."));
    }
  };

  const submitStatus = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      if (editingStatusId) {
        await axiosClient.put(`/api/catalog/statuses/${editingStatusId}`, statusForm);
        setMessage("Cập nhật trạng thái tài sản thành công.");
      } else {
        await axiosClient.post("/api/catalog/statuses", statusForm);
        setMessage("Thêm trạng thái tài sản thành công.");
      }

      setStatusForm(EMPTY_STATUS_FORM);
      setEditingStatusId(null);
      fetchCatalog();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không lưu được trạng thái tài sản."));
    }
  };

  const handleEditCategory = (category) => {
    setEditingCategoryId(category.categoryId);
    setCategoryForm({
      categoryCode: category.categoryCode,
      categoryName: category.categoryName,
      parentCategoryId: category.parentCategory?.id || "",
      defaultWarrantyMonths: category.defaultWarrantyMonths || "",
      defaultMaintenanceCycleDays: category.defaultMaintenanceCycleDays || "",
      description: category.description || "",
      isActive: category.isActive,
    });
  };

  const handleEditStatus = (status) => {
    setEditingStatusId(status.statusId);
    setStatusForm({
      statusCode: status.statusCode,
      statusName: status.statusName,
      statusGroup: status.statusGroup,
      isAllocatable: Boolean(status.isAllocatable),
      sortOrder: status.sortOrder || 0,
      description: status.description || "",
    });
  };

  const deactivateCategory = async (categoryId) => {
    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/catalog/categories/${categoryId}/deactivate`);
      setMessage("Đã ngừng sử dụng danh mục.");
      fetchCatalog();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không thể ngừng sử dụng danh mục."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Quản lý danh mục tài sản"
        description="Quản lý nhóm tài sản, chính sách mặc định và các trạng thái vận hành của tài sản."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Danh mục tài sản</h3>
            <p>Phục vụ tạo tài sản và áp dụng chính sách mặc định.</p>
          </div>

          <form className="form-grid" onSubmit={submitCategory}>
            <div>
              <label className="form-label">Mã danh mục</label>
              <input
                type="text"
                name="categoryCode"
                className="form-control"
                value={categoryForm.categoryCode}
                onChange={handleCategoryChange}
                required
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Tên danh mục</label>
              <input
                type="text"
                name="categoryName"
                className="form-control"
                value={categoryForm.categoryName}
                onChange={handleCategoryChange}
                required
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Danh mục cha</label>
              <select
                name="parentCategoryId"
                className="form-select"
                value={categoryForm.parentCategoryId}
                onChange={handleCategoryChange}
                disabled={!canManage}
              >
                <option value="">Không có</option>
                {categories
                  .filter((item) => item.categoryId !== editingCategoryId)
                  .map((item) => (
                    <option key={item.categoryId} value={item.categoryId}>
                      {item.categoryCode} - {item.categoryName}
                    </option>
                  ))}
              </select>
            </div>

            <div>
              <label className="form-label">Bảo hành mặc định (tháng)</label>
              <input
                type="number"
                name="defaultWarrantyMonths"
                className="form-control"
                value={categoryForm.defaultWarrantyMonths}
                onChange={handleCategoryChange}
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Chu kỳ bảo trì (ngày)</label>
              <input
                type="number"
                name="defaultMaintenanceCycleDays"
                className="form-control"
                value={categoryForm.defaultMaintenanceCycleDays}
                onChange={handleCategoryChange}
                disabled={!canManage}
              />
            </div>

            <div className="form-check inline-check">
              <input
                id="category-active"
                type="checkbox"
                name="isActive"
                className="form-check-input"
                checked={categoryForm.isActive}
                onChange={handleCategoryChange}
                disabled={!canManage}
              />
              <label htmlFor="category-active" className="form-check-label">
                Đang hoạt động
              </label>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mô tả</label>
              <textarea
                name="description"
                className="form-control"
                rows="3"
                value={categoryForm.description}
                onChange={handleCategoryChange}
                disabled={!canManage}
              />
            </div>

            {canManage ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingCategoryId ? "Cập nhật danh mục" : "Thêm danh mục"}
                </button>
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => {
                    setCategoryForm(EMPTY_CATEGORY_FORM);
                    setEditingCategoryId(null);
                  }}
                >
                  Đặt lại
                </button>
              </div>
            ) : null}
          </form>

          <div className="table-responsive mt-4">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã</th>
                  <th>Tên danh mục</th>
                  <th>Chính sách</th>
                  <th>Trạng thái</th>
                  {canManage ? <th className="text-end">Tác vụ</th> : null}
                </tr>
              </thead>
              <tbody>
                {categories.length ? (
                  categories.map((category) => (
                    <tr key={category.categoryId}>
                      <td>{category.categoryCode}</td>
                      <td>
                        <strong>{category.categoryName}</strong>
                        <div className="table-subline">
                          {category.parentCategory ? `Cha: ${category.parentCategory.name}` : "Không có danh mục cha"}
                        </div>
                      </td>
                      <td>
                        <div>{category.defaultWarrantyMonths || 0} tháng bảo hành</div>
                        <div className="table-subline">
                          {category.defaultMaintenanceCycleDays || 0} ngày / chu kỳ bảo trì
                        </div>
                      </td>
                      <td>
                        <StatusBadge status={category.isActive ? "ACTIVE" : "INACTIVE"} />
                      </td>
                      {canManage ? (
                        <td className="text-end">
                          <div className="table-actions">
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => handleEditCategory(category)}
                            >
                              Sửa
                            </button>
                            {category.isActive ? (
                              <button
                                type="button"
                                className="btn btn-sm btn-outline-danger"
                                onClick={() => deactivateCategory(category.categoryId)}
                              >
                                Ngừng dùng
                              </button>
                            ) : null}
                          </div>
                        </td>
                      ) : null}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={canManage ? 5 : 4} className="text-center text-muted">
                      {loading ? "Đang tải..." : "Chưa có danh mục tài sản."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card">
          <div className="card-head">
            <h3>Trạng thái tài sản</h3>
            <p>Quy định trạng thái nghiệp vụ và khả năng cấp phát.</p>
          </div>

          <form className="form-grid" onSubmit={submitStatus}>
            <div>
              <label className="form-label">Mã trạng thái</label>
              <input
                type="text"
                name="statusCode"
                className="form-control"
                value={statusForm.statusCode}
                onChange={handleStatusChange}
                required
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Tên trạng thái</label>
              <input
                type="text"
                name="statusName"
                className="form-control"
                value={statusForm.statusName}
                onChange={handleStatusChange}
                required
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Nhóm trạng thái</label>
              <select
                name="statusGroup"
                className="form-select"
                value={statusForm.statusGroup}
                onChange={handleStatusChange}
                disabled={!canManage}
              >
                <option value="STORAGE">Lưu kho</option>
                <option value="USAGE">Sử dụng</option>
                <option value="MAINTENANCE">Bảo trì</option>
                <option value="END_OF_LIFE">Kết thúc vòng đời</option>
                <option value="EXCEPTION">Ngoại lệ</option>
              </select>
            </div>

            <div>
              <label className="form-label">Thứ tự hiển thị</label>
              <input
                type="number"
                name="sortOrder"
                className="form-control"
                value={statusForm.sortOrder}
                onChange={handleStatusChange}
                disabled={!canManage}
              />
            </div>

            <div className="form-check inline-check">
              <input
                id="status-allocatable"
                type="checkbox"
                name="isAllocatable"
                className="form-check-input"
                checked={statusForm.isAllocatable}
                onChange={handleStatusChange}
                disabled={!canManage}
              />
              <label htmlFor="status-allocatable" className="form-check-label">
                Có thể cấp phát
              </label>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mô tả</label>
              <textarea
                name="description"
                className="form-control"
                rows="3"
                value={statusForm.description}
                onChange={handleStatusChange}
                disabled={!canManage}
              />
            </div>

            {canManage ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingStatusId ? "Cập nhật trạng thái" : "Thêm trạng thái"}
                </button>
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => {
                    setStatusForm(EMPTY_STATUS_FORM);
                    setEditingStatusId(null);
                  }}
                >
                  Đặt lại
                </button>
              </div>
            ) : null}
          </form>

          <div className="table-responsive mt-4">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã</th>
                  <th>Tên trạng thái</th>
                  <th>Nhóm</th>
                  <th>Cấp phát</th>
                  {canManage ? <th className="text-end">Tác vụ</th> : null}
                </tr>
              </thead>
              <tbody>
                {statuses.length ? (
                  statuses.map((status) => (
                    <tr key={status.statusId}>
                      <td>{status.statusCode}</td>
                      <td>{status.statusName}</td>
                      <td>{STATUS_GROUP_LABELS[status.statusGroup] || status.statusGroup}</td>
                      <td>{status.isAllocatable ? "Có" : "Không"}</td>
                      {canManage ? (
                        <td className="text-end">
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => handleEditStatus(status)}
                          >
                            Sửa
                          </button>
                        </td>
                      ) : null}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={canManage ? 5 : 4} className="text-center text-muted">
                      {loading ? "Đang tải..." : "Chưa có trạng thái tài sản."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  );
}

export default CatalogManagement;
