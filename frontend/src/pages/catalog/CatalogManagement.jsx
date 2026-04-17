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
      setError(getApiErrorMessage(requestError, "Khong tai duoc du lieu danh muc."));
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
        setMessage("Cap nhat danh muc tai san thanh cong.");
      } else {
        await axiosClient.post("/api/catalog/categories", {
          ...categoryForm,
          parentCategoryId: categoryForm.parentCategoryId || null,
          defaultWarrantyMonths: categoryForm.defaultWarrantyMonths || null,
          defaultMaintenanceCycleDays: categoryForm.defaultMaintenanceCycleDays || null,
        });
        setMessage("Them danh muc tai san thanh cong.");
      }

      setCategoryForm(EMPTY_CATEGORY_FORM);
      setEditingCategoryId(null);
      fetchCatalog();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong luu duoc danh muc tai san."));
    }
  };

  const submitStatus = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      if (editingStatusId) {
        await axiosClient.put(`/api/catalog/statuses/${editingStatusId}`, statusForm);
        setMessage("Cap nhat trang thai tai san thanh cong.");
      } else {
        await axiosClient.post("/api/catalog/statuses", statusForm);
        setMessage("Them trang thai tai san thanh cong.");
      }

      setStatusForm(EMPTY_STATUS_FORM);
      setEditingStatusId(null);
      fetchCatalog();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong luu duoc trang thai tai san."));
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
      setMessage("Da ngung su dung danh muc.");
      fetchCatalog();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong the ngung su dung danh muc."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Quan ly danh muc tai san"
        description="Quan ly nhom tai san, chinh sach mac dinh va cac trang thai van hanh cua tai san."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Danh muc tai san</h3>
            <p>Phuc vu tao tai san va ap dung chinh sach mac dinh.</p>
          </div>

          <form className="form-grid" onSubmit={submitCategory}>
            <div>
              <label className="form-label">Ma danh muc</label>
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
              <label className="form-label">Ten danh muc</label>
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
              <label className="form-label">Danh muc cha</label>
              <select
                name="parentCategoryId"
                className="form-select"
                value={categoryForm.parentCategoryId}
                onChange={handleCategoryChange}
                disabled={!canManage}
              >
                <option value="">Khong co</option>
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
              <label className="form-label">Bao hanh mac dinh (thang)</label>
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
              <label className="form-label">Chu ky bao tri (ngay)</label>
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
                Dang hoat dong
              </label>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mo ta</label>
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
                  {editingCategoryId ? "Cap nhat danh muc" : "Them danh muc"}
                </button>
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => {
                    setCategoryForm(EMPTY_CATEGORY_FORM);
                    setEditingCategoryId(null);
                  }}
                >
                  Dat lai
                </button>
              </div>
            ) : null}
          </form>

          <div className="table-responsive mt-4">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma</th>
                  <th>Ten danh muc</th>
                  <th>Chinh sach</th>
                  <th>Trang thai</th>
                  {canManage ? <th className="text-end">Tac vu</th> : null}
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
                          {category.parentCategory ? `Cha: ${category.parentCategory.name}` : "Khong co danh muc cha"}
                        </div>
                      </td>
                      <td>
                        <div>{category.defaultWarrantyMonths || 0} thang bao hanh</div>
                        <div className="table-subline">
                          {category.defaultMaintenanceCycleDays || 0} ngay / chu ky bao tri
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
                              Sua
                            </button>
                            {category.isActive ? (
                              <button
                                type="button"
                                className="btn btn-sm btn-outline-danger"
                                onClick={() => deactivateCategory(category.categoryId)}
                              >
                                Ngung dung
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
                      {loading ? "Dang tai..." : "Chua co danh muc tai san."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card">
          <div className="card-head">
            <h3>Trang thai tai san</h3>
            <p>Quy dinh trang thai nghiep vu va kha nang cap phat.</p>
          </div>

          <form className="form-grid" onSubmit={submitStatus}>
            <div>
              <label className="form-label">Ma trang thai</label>
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
              <label className="form-label">Ten trang thai</label>
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
              <label className="form-label">Nhom trang thai</label>
              <select
                name="statusGroup"
                className="form-select"
                value={statusForm.statusGroup}
                onChange={handleStatusChange}
                disabled={!canManage}
              >
                <option value="STORAGE">STORAGE</option>
                <option value="USAGE">USAGE</option>
                <option value="MAINTENANCE">MAINTENANCE</option>
                <option value="END_OF_LIFE">END_OF_LIFE</option>
                <option value="EXCEPTION">EXCEPTION</option>
              </select>
            </div>

            <div>
              <label className="form-label">Thu tu hien thi</label>
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
                Co the cap phat
              </label>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mo ta</label>
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
                  {editingStatusId ? "Cap nhat trang thai" : "Them trang thai"}
                </button>
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => {
                    setStatusForm(EMPTY_STATUS_FORM);
                    setEditingStatusId(null);
                  }}
                >
                  Dat lai
                </button>
              </div>
            ) : null}
          </form>

          <div className="table-responsive mt-4">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma</th>
                  <th>Ten trang thai</th>
                  <th>Nhom</th>
                  <th>Cap phat</th>
                  {canManage ? <th className="text-end">Tac vu</th> : null}
                </tr>
              </thead>
              <tbody>
                {statuses.length ? (
                  statuses.map((status) => (
                    <tr key={status.statusId}>
                      <td>{status.statusCode}</td>
                      <td>{status.statusName}</td>
                      <td>{status.statusGroup}</td>
                      <td>{status.isAllocatable ? "Co" : "Khong"}</td>
                      {canManage ? (
                        <td className="text-end">
                          <button
                            type="button"
                            className="btn btn-sm btn-outline-primary"
                            onClick={() => handleEditStatus(status)}
                          >
                            Sua
                          </button>
                        </td>
                      ) : null}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={canManage ? 5 : 4} className="text-center text-muted">
                      {loading ? "Dang tai..." : "Chua co trang thai tai san."}
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
