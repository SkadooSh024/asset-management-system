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
      setError(getApiErrorMessage(requestError, "Khong tai duoc danh sach tai san."));
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
      setError(getApiErrorMessage(requestError, "Khong tai duoc chi tiet tai san."));
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
        setMessage("Cap nhat tai san thanh cong.");
      } else {
        await axiosClient.post("/api/assets", payload);
        setMessage("Them tai san thanh cong.");
      }

      resetForm();
      fetchAssets();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong luu duoc tai san."));
    }
  };

  const handleRetireAsset = async (assetId) => {
    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/assets/${assetId}/retire`, {
        actingUserId: user.userId,
        note: "Danh dau thanh ly tai san tu giao dien quan ly.",
      });
      setMessage("Da danh dau thanh ly tai san.");
      fetchAssets();
      if (selectedAsset?.assetId === assetId) {
        setSelectedAsset(null);
      }
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong the thanh ly tai san."));
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
        title="Quan ly tai san"
        description="Quan ly ho so tai san, thong tin su dung hien tai va lich su bien dong."
        action={
          canManage ? (
            <button type="button" className="btn btn-primary" onClick={resetForm}>
              Them tai san moi
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
            placeholder="Tim theo ma, ten hoac brand..."
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
          />
          <select
            className="form-select"
            value={categoryFilter}
            onChange={(event) => setCategoryFilter(event.target.value)}
          >
            <option value="">Tat ca danh muc</option>
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
            <option value="">Tat ca trang thai</option>
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
            <h3>Danh sach tai san</h3>
            <p>Chon 1 tai san de xem chi tiet va lich su.</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma tai san</th>
                  <th>Ten tai san</th>
                  <th>Danh muc</th>
                  <th>Trang thai</th>
                  <th>Phong ban</th>
                  {canManage ? <th className="text-end">Tac vu</th> : null}
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
                              Sua
                            </button>
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-danger"
                              onClick={(event) => {
                                event.stopPropagation();
                                handleRetireAsset(asset.assetId);
                              }}
                            >
                              Thanh ly
                            </button>
                          </div>
                        </td>
                      ) : null}
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={canManage ? 6 : 5} className="text-center text-muted">
                      {loading ? "Dang tai..." : "Khong co tai san phu hop bo loc."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card">
          <div className="card-head">
            <h3>{editingAssetId ? "Cap nhat tai san" : "Them tai san"}</h3>
            <p>{canManage ? "Luu y: ma tai san phai duy nhat." : "Tai khoan nay chi co quyen xem."}</p>
          </div>

          <form className="form-grid" onSubmit={handleSubmit}>
            <div>
              <label className="form-label">Ma tai san</label>
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
              <label className="form-label">Ten tai san</label>
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
              <label className="form-label">Danh muc</label>
              <select
                className="form-select"
                name="categoryId"
                value={assetForm.categoryId}
                onChange={handleInputChange}
                required
                disabled={!canManage}
              >
                <option value="">Chon danh muc</option>
                {lookups?.assetCategories?.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Trang thai</label>
              <select
                className="form-select"
                name="currentStatusId"
                value={assetForm.currentStatusId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Mac dinh theo nghiep vu</option>
                {lookups?.assetStatuses?.map((status) => (
                  <option key={status.id} value={status.id}>
                    {status.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Phong ban so huu</label>
              <select
                className="form-select"
                name="owningDepartmentId"
                value={assetForm.owningDepartmentId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Chon phong ban</option>
                {lookups?.departments?.map((department) => (
                  <option key={department.id} value={department.id}>
                    {department.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Phong ban hien tai</label>
              <select
                className="form-select"
                name="currentDepartmentId"
                value={assetForm.currentDepartmentId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Chon phong ban</option>
                {lookups?.departments?.map((department) => (
                  <option key={department.id} value={department.id}>
                    {department.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Nguoi su dung hien tai</label>
              <select
                className="form-select"
                name="assignedUserId"
                value={assetForm.assignedUserId}
                onChange={handleInputChange}
                disabled={!canManage}
              >
                <option value="">Chon nguoi dung</option>
                {lookups?.users?.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.fullName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Brand</label>
              <input type="text" className="form-control" name="brand" value={assetForm.brand} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Model</label>
              <input type="text" className="form-control" name="model" value={assetForm.model} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Serial number</label>
              <input type="text" className="form-control" name="serialNumber" value={assetForm.serialNumber} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Asset tag</label>
              <input type="text" className="form-control" name="assetTag" value={assetForm.assetTag} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Ngay mua</label>
              <input type="date" className="form-control" name="purchaseDate" value={assetForm.purchaseDate} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Het bao hanh</label>
              <input type="date" className="form-control" name="warrantyExpiryDate" value={assetForm.warrantyExpiryDate} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Gia mua</label>
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
                Dang hoat dong
              </label>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Thong so ky thuat</label>
              <textarea className="form-control" rows="3" name="specificationText" value={assetForm.specificationText} onChange={handleInputChange} disabled={!canManage} />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Ghi chu</label>
              <textarea className="form-control" rows="3" name="notes" value={assetForm.notes} onChange={handleInputChange} disabled={!canManage} />
            </div>

            {canManage ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingAssetId ? "Cap nhat tai san" : "Luu tai san"}
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={resetForm}>
                  Dat lai
                </button>
              </div>
            ) : null}
          </form>
        </section>

        <section className="content-card content-card--full">
          <div className="card-head">
            <h3>Chi tiet va lich su</h3>
            <p>Chon 1 tai san trong bang de xem nhat ky bien dong.</p>
          </div>

          {selectedAsset ? (
            <div className="detail-stack">
              <div className="detail-grid">
                <div>
                  <span className="detail-label">Ten tai san</span>
                  <strong>{selectedAsset.assetName}</strong>
                </div>
                <div>
                  <span className="detail-label">Ma tai san</span>
                  <strong>{selectedAsset.assetCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Trang thai</span>
                  <StatusBadge status={selectedAsset.currentStatus?.code} />
                </div>
                <div>
                  <span className="detail-label">Gia mua</span>
                  <strong>{formatCurrency(selectedAsset.purchaseCost)}</strong>
                </div>
                <div>
                  <span className="detail-label">Ngay mua</span>
                  <strong>{formatDate(selectedAsset.purchaseDate)}</strong>
                </div>
                <div>
                  <span className="detail-label">Cap nhat luc</span>
                  <strong>{formatDateTime(selectedAsset.updatedAt)}</strong>
                </div>
              </div>

              <div className="table-responsive">
                <table className="table align-middle custom-table">
                  <thead>
                    <tr>
                      <th>Thoi gian</th>
                      <th>Hanh dong</th>
                      <th>Tu</th>
                      <th>Den</th>
                      <th>Mo ta</th>
                    </tr>
                  </thead>
                  <tbody>
                    {selectedAsset.histories?.length ? (
                      selectedAsset.histories.map((history) => (
                        <tr key={history.assetHistoryId}>
                          <td>{formatDateTime(history.actionTime)}</td>
                          <td>{history.actionType}</td>
                          <td>{history.fromStatus?.name || history.fromDepartment?.name || history.fromUser?.fullName || "--"}</td>
                          <td>{history.toStatus?.name || history.toDepartment?.name || history.toUser?.fullName || "--"}</td>
                          <td>{history.description || "--"}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="5" className="text-center text-muted">
                          Chua co lich su bien dong.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            <div className="empty-panel">
              Chon 1 tai san trong danh sach de xem thong tin chi tiet va lich su.
            </div>
          )}
        </section>
      </div>
    </div>
  );
}

export default AssetManagement;
