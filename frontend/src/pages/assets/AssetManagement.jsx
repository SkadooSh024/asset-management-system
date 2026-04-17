import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import { getStoredUser } from "../../utils/auth";
import { formatCurrency, formatDate, formatDateTime, getApiErrorMessage } from "../../utils/format";

const EMPTY_ASSET_FORM = {
  assetCode: "",
  assetName: "",
  categoryId: "",
  currentStatusId: "",
  owningDepartmentId: "",
  currentDepartmentId: "",
  assignedUserId: "",
  brand: "",
  model: "",
  serialNumber: "",
  assetTag: "",
  purchaseDate: "",
  warrantyExpiryDate: "",
  purchaseCost: "",
  specificationText: "",
  notes: "",
  imageUrl: "",
  isActive: true,
};

const HISTORY_ACTION_LABELS = {
  CREATE: "Khởi tạo",
  UPDATE: "Cập nhật",
  RETIRE: "Thanh lý",
  ASSIGNMENT: "Cấp phát",
  INCIDENT: "Ghi nhận sự cố",
  INCIDENT_CLOSE: "Đóng sự cố",
  MAINTENANCE: "Mở phiếu bảo trì",
  MAINTENANCE_COMPLETE: "Hoàn tất bảo trì",
};

function AssetManagement() {
  const user = getStoredUser();
  const [assets, setAssets] = useState([]);
  const [lookups, setLookups] = useState(null);
  const [selectedAsset, setSelectedAsset] = useState(null);
  const [assetForm, setAssetForm] = useState(EMPTY_ASSET_FORM);
  const [editingAssetId, setEditingAssetId] = useState(null);
  const [keyword, setKeyword] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const canManage = ["ADMIN", "ASSET_STAFF"].includes(user?.roleCode);

  const fetchAssets = async () => {
    setLoading(true);
    setError("");

    try {
      const [assetsResponse, lookupsResponse] = await Promise.all([
        axiosClient.get("/api/assets"),
        axiosClient.get("/api/lookups/options"),
      ]);

      setAssets(assetsResponse.data);
      setLookups(lookupsResponse.data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được danh sách tài sản."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAssets();
  }, []);

  const handleInputChange = (event) => {
    const { name, value, type, checked } = event.target;
    setAssetForm((current) => ({
      ...current,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSelectAsset = async (assetId) => {
    try {
      const { data } = await axiosClient.get(`/api/assets/${assetId}`);
      setSelectedAsset(data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được chi tiết tài sản."));
    }
  };

  const handleEditAsset = (asset) => {
    setEditingAssetId(asset.assetId);
    setAssetForm({
      assetCode: asset.assetCode || "",
      assetName: asset.assetName || "",
      categoryId: asset.category?.id || "",
      currentStatusId: asset.currentStatus?.id || "",
      owningDepartmentId: asset.owningDepartment?.id || "",
      currentDepartmentId: asset.currentDepartment?.id || "",
      assignedUserId: asset.assignedUser?.id || "",
      brand: asset.brand || "",
      model: asset.model || "",
      serialNumber: asset.serialNumber || "",
      assetTag: asset.assetTag || "",
      purchaseDate: asset.purchaseDate || "",
      warrantyExpiryDate: asset.warrantyExpiryDate || "",
      purchaseCost: asset.purchaseCost || "",
      specificationText: asset.specificationText || "",
      notes: asset.notes || "",
      imageUrl: asset.imageUrl || "",
      isActive: asset.isActive ?? true,
    });
    handleSelectAsset(asset.assetId);
  };

  const resetForm = () => {
    setAssetForm(EMPTY_ASSET_FORM);
    setEditingAssetId(null);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    const payload = {
      ...assetForm,
      categoryId: Number(assetForm.categoryId),
      currentStatusId: assetForm.currentStatusId ? Number(assetForm.currentStatusId) : null,
      owningDepartmentId: assetForm.owningDepartmentId ? Number(assetForm.owningDepartmentId) : null,
      currentDepartmentId: assetForm.currentDepartmentId ? Number(assetForm.currentDepartmentId) : null,
      assignedUserId: assetForm.assignedUserId ? Number(assetForm.assignedUserId) : null,
      purchaseCost: assetForm.purchaseCost || null,
      actingUserId: user.userId,
    };

    try {
      if (editingAssetId) {
        await axiosClient.put(`/api/assets/${editingAssetId}`, payload);
        setMessage("Cập nhật tài sản thành công.");
      } else {
        await axiosClient.post("/api/assets", payload);
        setMessage("Thêm tài sản thành công.");
      }

      resetForm();
      fetchAssets();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không lưu được tài sản."));
    }
  };

  const handleRetireAsset = async (assetId) => {
    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/assets/${assetId}/retire`, {
        actingUserId: user.userId,
        note: "Đánh dấu thanh lý tài sản từ giao diện quản lý.",
      });
      setMessage("Đã đánh dấu thanh lý tài sản.");
      fetchAssets();
      if (selectedAsset?.assetId === assetId) {
        setSelectedAsset(null);
      }
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không thể thanh lý tài sản."));
    }
  };

  const filteredAssets = assets.filter((asset) => {
    const matchKeyword =
      !keyword ||
      asset.assetCode?.toLowerCase().includes(keyword.toLowerCase()) ||
      asset.assetName?.toLowerCase().includes(keyword.toLowerCase()) ||
      asset.brand?.toLowerCase().includes(keyword.toLowerCase());
    const matchCategory = !categoryFilter || String(asset.category?.id) === categoryFilter;
    const matchStatus = !statusFilter || String(asset.currentStatus?.id) === statusFilter;
    return matchKeyword && matchCategory && matchStatus;
  });

  return (
    <div className="page-stack">
      <PageHeader
        title="Quản lý tài sản"
        description="Quản lý hồ sơ tài sản, thông tin sử dụng hiện tại và lịch sử biến động."
        action={
          canManage ? (
            <button type="button" className="btn btn-primary" onClick={resetForm}>
              Thêm tài sản mới
            </button>
          ) : null
        }
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <section className="content-card">
        <div className="filter-grid">
          <input
            type="text"
            className="form-control"
            placeholder="Tìm theo mã, tên hoặc hãng..."
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
          />
          <select
            className="form-select"
            value={categoryFilter}
            onChange={(event) => setCategoryFilter(event.target.value)}
          >
            <option value="">Tất cả danh mục</option>
            {lookups?.assetCategories?.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
          <select
            className="form-select"
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value)}
          >
            <option value="">Tất cả trạng thái</option>
            {lookups?.assetStatuses?.map((status) => (
              <option key={status.id} value={status.id}>
                {status.name}
              </option>
            ))}
          </select>
        </div>
      </section>

      <div className="content-grid">
        <section className="content-card content-card--wide">
          <div className="card-head">
            <h3>Danh sách tài sản</h3>
            <p>Chọn 1 tài sản để xem chi tiết và lịch sử.</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã tài sản</th>
                  <th>Tên tài sản</th>
                  <th>Danh mục</th>
                  <th>Trạng thái</th>
                  <th>Phòng ban</th>
                  {canManage ? <th className="text-end">Tác vụ</th> : null}
                </tr>
              </thead>
              <tbody>
                {filteredAssets.length ? (
                  filteredAssets.map((asset) => (
                    <tr key={asset.assetId} onClick={() => handleSelectAsset(asset.assetId)} className="clickable-row">
                      <td>{asset.assetCode}</td>
                      <td>
                        <strong>{asset.assetName}</strong>
                        <div className="table-subline">{asset.brand || "--"} / {asset.model || "--"}</div>
                      </td>
                      <td>{asset.category?.name}</td>
                      <td><StatusBadge status={asset.currentStatus?.code} /></td>
                      <td>{asset.currentDepartment?.name || asset.owningDepartment?.name || "--"}</td>
                      {canManage ? (
                        <td className="text-end">
                          <div className="table-actions">
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-primary"
                              onClick={(event) => {
                                event.stopPropagation();
                                handleEditAsset(asset);
                              }}
                            >
                              Sửa
                            </button>
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-danger"
                              onClick={(event) => {
                                event.stopPropagation();
                                handleRetireAsset(asset.assetId);
                              }}
                            >
                              Thanh lý
                            </button>
                          </div>
                        </td>
                      ) : null}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={canManage ? 6 : 5} className="text-center text-muted">
                      {loading ? "Đang tải..." : "Không có tài sản phù hợp bộ lọc."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card">
          <div className="card-head">
            <h3>{editingAssetId ? "Cập nhật tài sản" : "Thêm tài sản"}</h3>
            <p>{canManage ? "Lưu ý: mã tài sản phải duy nhất." : "Tài khoản này chỉ có quyền xem."}</p>
          </div>

          <form className="form-grid" onSubmit={handleSubmit}>
            <div>
              <label className="form-label">Mã tài sản</label>
              <input
                type="text"
                className="form-control"
                name="assetCode"
                value={assetForm.assetCode}
                onChange={handleInputChange}
                required
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Tên tài sản</label>
              <input
                type="text"
                className="form-control"
                name="assetName"
                value={assetForm.assetName}
                onChange={handleInputChange}
                required
                disabled={!canManage}
              />
            </div>

            <div>
              <label className="form-label">Danh mục</label>
              <select
                className="form-select"
                name="categoryId"
                value={assetForm.categoryId}
                onChange={handleInputChange}
                required
                disabled={!canManage}
              >
                <option value="">Chọn danh mục</option>
                {lookups?.assetCategories?.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Trạng thái</label>
              <select
                className="form-select"
                name="currentStatusId"
                value={assetForm.currentStatusId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Mặc định theo nghiệp vụ</option>
                {lookups?.assetStatuses?.map((status) => (
                  <option key={status.id} value={status.id}>
                    {status.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Phòng ban sở hữu</label>
              <select
                className="form-select"
                name="owningDepartmentId"
                value={assetForm.owningDepartmentId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Chọn phòng ban</option>
                {lookups?.departments?.map((department) => (
                  <option key={department.id} value={department.id}>
                    {department.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Phòng ban hiện tại</label>
              <select
                className="form-select"
                name="currentDepartmentId"
                value={assetForm.currentDepartmentId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Chọn phòng ban</option>
                {lookups?.departments?.map((department) => (
                  <option key={department.id} value={department.id}>
                    {department.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Người sử dụng hiện tại</label>
              <select
                className="form-select"
                name="assignedUserId"
                value={assetForm.assignedUserId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Chọn người dùng</option>
                {lookups?.users?.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.fullName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Hãng</label>
              <input type="text" className="form-control" name="brand" value={assetForm.brand} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Dòng máy</label>
              <input type="text" className="form-control" name="model" value={assetForm.model} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Số serial</label>
              <input type="text" className="form-control" name="serialNumber" value={assetForm.serialNumber} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Mã thẻ tài sản</label>
              <input type="text" className="form-control" name="assetTag" value={assetForm.assetTag} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Ngày mua</label>
              <input type="date" className="form-control" name="purchaseDate" value={assetForm.purchaseDate} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Ngày hết bảo hành</label>
              <input type="date" className="form-control" name="warrantyExpiryDate" value={assetForm.warrantyExpiryDate} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Giá mua</label>
              <input type="number" className="form-control" name="purchaseCost" value={assetForm.purchaseCost} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div className="form-check inline-check">
              <input
                id="asset-active"
                type="checkbox"
                className="form-check-input"
                name="isActive"
                checked={assetForm.isActive}
                onChange={handleInputChange}
                disabled={!canManage}
              />
              <label htmlFor="asset-active" className="form-check-label">
                Đang hoạt động
              </label>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Thông số kỹ thuật</label>
              <textarea className="form-control" rows="3" name="specificationText" value={assetForm.specificationText} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Ghi chú</label>
              <textarea className="form-control" rows="3" name="notes" value={assetForm.notes} onChange={handleInputChange} disabled={!canManage} />
            </div>

            {canManage ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingAssetId ? "Cập nhật tài sản" : "Lưu tài sản"}
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={resetForm}>
                  Đặt lại
                </button>
              </div>
            ) : null}
          </form>
        </section>

        <section className="content-card content-card--full">
          <div className="card-head">
            <h3>Chi tiết và lịch sử</h3>
            <p>Chọn 1 tài sản trong bảng để xem nhật ký biến động.</p>
          </div>

          {selectedAsset ? (
            <div className="detail-stack">
              <div className="detail-grid">
                <div>
                  <span className="detail-label">Tên tài sản</span>
                  <strong>{selectedAsset.assetName}</strong>
                </div>
                <div>
                  <span className="detail-label">Mã tài sản</span>
                  <strong>{selectedAsset.assetCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Trạng thái</span>
                  <StatusBadge status={selectedAsset.currentStatus?.code} />
                </div>
                <div>
                  <span className="detail-label">Giá mua</span>
                  <strong>{formatCurrency(selectedAsset.purchaseCost)}</strong>
                </div>
                <div>
                  <span className="detail-label">Ngày mua</span>
                  <strong>{formatDate(selectedAsset.purchaseDate)}</strong>
                </div>
                <div>
                  <span className="detail-label">Cập nhật lúc</span>
                  <strong>{formatDateTime(selectedAsset.updatedAt)}</strong>
                </div>
              </div>

              <div className="table-responsive">
                <table className="table align-middle custom-table">
                  <thead>
                    <tr>
                      <th>Thời gian</th>
                      <th>Hành động</th>
                      <th>Từ</th>
                      <th>Đến</th>
                      <th>Mô tả</th>
                    </tr>
                  </thead>
                  <tbody>
                    {selectedAsset.histories?.length ? (
                      selectedAsset.histories.map((history) => (
                        <tr key={history.assetHistoryId}>
                          <td>{formatDateTime(history.actionTime)}</td>
                          <td>{HISTORY_ACTION_LABELS[history.actionType] || history.actionType}</td>
                          <td>{history.fromStatus?.name || history.fromDepartment?.name || history.fromUser?.fullName || "--"}</td>
                          <td>{history.toStatus?.name || history.toDepartment?.name || history.toUser?.fullName || "--"}</td>
                          <td>{history.description || "--"}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="5" className="text-center text-muted">
                          Chưa có lịch sử biến động.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            <div className="empty-panel">
              Chọn 1 tài sản trong danh sách để xem thông tin chi tiết và lịch sử.
            </div>
          )}
        </section>
      </div>
    </div>
  );
}

export default AssetManagement;
