import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import useFeedbackToast from "../../hooks/useFeedbackToast";
import { getStoredUser, hasAnyRole } from "../../utils/auth";
import { formatDate, getApiErrorMessage } from "../../utils/format";
import { ACTION_ACCESS } from "../../config/roleAccess";

function AssignmentManagement() {
  const user = getStoredUser();
  const [assignments, setAssignments] = useState([]);
  const [assets, setAssets] = useState([]);
  const [lookups, setLookups] = useState(null);
  const [selectedAssignment, setSelectedAssignment] = useState(null);
  const [form, setForm] = useState({
    assignmentDate: new Date().toISOString().slice(0, 10),
    sourceDepartmentId: "",
    targetDepartmentId: "",
    targetUserId: "",
    reason: "",
    note: "",
    assetIds: [],
  });
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useFeedbackToast({ successMessage: message, errorMessage: error });

  const canCreate = hasAnyRole(user, ACTION_ACCESS.ASSIGNMENT_CREATE);
  const canApprove = hasAnyRole(user, ACTION_ACCESS.ASSIGNMENT_APPROVE);
  const canComplete = hasAnyRole(user, ACTION_ACCESS.ASSIGNMENT_COMPLETE);

  const fetchData = async () => {
    setLoading(true);
    setError("");

    try {
      const [assignmentsResponse, assetsResponse, lookupsResponse] = await Promise.all([
        axiosClient.get("/api/assignments"),
        axiosClient.get("/api/assets"),
        axiosClient.get("/api/lookups/options"),
      ]);

      setAssignments(assignmentsResponse.data);
      setAssets(assetsResponse.data);
      setLookups(lookupsResponse.data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được dữ liệu cấp phát."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const availableAssets = assets.filter(
    (asset) => asset.isActive && asset.currentStatus?.code === "READY"
  );

  const selectedAssets = availableAssets.filter((asset) => form.assetIds.includes(asset.assetId));
  const selectedSourceDepartments = selectedAssets.reduce((departments, asset) => {
    const sourceDepartment = asset.currentDepartment || asset.owningDepartment;
    if (!sourceDepartment) {
      return departments;
    }

    if (!departments.some((item) => item.id === sourceDepartment.id)) {
      departments.push(sourceDepartment);
    }

    return departments;
  }, []);

  const derivedSourceDepartmentId =
    selectedSourceDepartments.length === 1 ? String(selectedSourceDepartments[0].id) : "";
  const derivedSourceDepartmentLabel =
    selectedAssets.length === 0
      ? "Chọn tài sản để hệ thống tự suy ra"
      : selectedSourceDepartments.length === 0
        ? "Không xác định từ dữ liệu hiện tại"
        : selectedSourceDepartments.length === 1
          ? selectedSourceDepartments[0].name
          : "Nhiều phòng ban nguồn";

  const filteredUsers = (lookups?.users || []).filter((item) =>
    form.targetDepartmentId ? String(item.departmentId) === form.targetDepartmentId : false
  );

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => {
      if (name === "targetDepartmentId") {
        return {
          ...current,
          targetDepartmentId: value,
          targetUserId:
            value && String(current.targetDepartmentId) === value ? current.targetUserId : "",
        };
      }

      return {
        ...current,
        [name]: value,
      };
    });
  };

  const toggleAsset = (assetId) => {
    setForm((current) => ({
      ...current,
      assetIds: current.assetIds.includes(assetId)
        ? current.assetIds.filter((item) => item !== assetId)
        : [...current.assetIds, assetId],
    }));
  };

  const handleCreate = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    if (!form.targetDepartmentId) {
      setError("Vui lòng chọn phòng ban nhận.");
      return;
    }

    if (!form.assetIds.length) {
      setError("Vui lòng chọn ít nhất 1 tài sản để cấp phát.");
      return;
    }

    try {
      await axiosClient.post("/api/assignments", {
        actingUserId: user.userId,
        assignmentDate: form.assignmentDate,
        sourceDepartmentId: derivedSourceDepartmentId || null,
        targetDepartmentId: Number(form.targetDepartmentId),
        targetUserId: form.targetUserId ? Number(form.targetUserId) : null,
        reason: form.reason,
        note: form.note,
        details: form.assetIds.map((assetId) => ({
          assetId,
        })),
      });

      setMessage("Tạo phiếu cấp phát thành công.");
      setForm({
        assignmentDate: new Date().toISOString().slice(0, 10),
        sourceDepartmentId: "",
        targetDepartmentId: "",
        targetUserId: "",
        reason: "",
        note: "",
        assetIds: [],
      });
      fetchData();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tạo được phiếu cấp phát."));
    }
  };

  const handleAction = async (assignmentId, action) => {
    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/assignments/${assignmentId}/${action}`, {
        actingUserId: user.userId,
      });
      setMessage(action === "approve" ? "Phê duyệt thành công." : "Hoàn tất cấp phát thành công.");
      fetchData();
      if (selectedAssignment?.assignmentFormId === assignmentId) {
        const { data } = await axiosClient.get(`/api/assignments/${assignmentId}`);
        setSelectedAssignment(data);
      }
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không thực hiện được thao tác."));
    }
  };

  const handleSelectAssignment = async (assignmentId) => {
    try {
      const { data } = await axiosClient.get(`/api/assignments/${assignmentId}`);
      setSelectedAssignment(data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được chi tiết phiếu cấp phát."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Cấp phát tài sản"
        description="Thực hiện luồng tạo phiếu, phê duyệt và hoàn tất cấp phát tài sản."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Tạo phiếu cấp phát</h3>
            <p>Chọn tài sản ở trạng thái sẵn sàng để lập phiếu mới.</p>
          </div>

          <form className="form-grid" onSubmit={handleCreate}>
            <div>
              <label className="form-label">Ngày cấp phát</label>
              <input
                type="date"
                className="form-control"
                name="assignmentDate"
                value={form.assignmentDate}
                onChange={handleChange}
                disabled={!canCreate}
                required
              />
            </div>

            <div>
              <label className="form-label">Phòng ban nguồn</label>
              <input
                type="text"
                className="form-control"
                name="sourceDepartmentId"
                value={derivedSourceDepartmentLabel}
                disabled
              />
              <small className="form-helper">Hệ thống tự suy ra từ tài sản đã chọn.</small>
            </div>

            <div>
              <label className="form-label">Phòng ban nhận</label>
              <select
                className="form-select"
                name="targetDepartmentId"
                value={form.targetDepartmentId}
                onChange={handleChange}
                disabled={!canCreate}
                required
              >
                <option value="">Bắt buộc chọn</option>
                {lookups?.departments?.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.name}
                  </option>
                ))}
              </select>
              <small className="form-helper">Chọn phòng ban trước để lọc người nhận cho dễ.</small>
            </div>

            <div>
              <label className="form-label">Người nhận</label>
              <select
                className="form-select"
                name="targetUserId"
                value={form.targetUserId}
                onChange={handleChange}
                disabled={!canCreate || !form.targetDepartmentId}
              >
                <option value="">
                  {form.targetDepartmentId
                    ? "Chọn người nhận trong phòng ban"
                    : "Chọn phòng ban nhận trước"}
                </option>
                {filteredUsers.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.fullName} - {item.departmentName || "Không có phòng ban"}
                  </option>
                ))}
              </select>
              <small className="form-helper">
                Có thể để trống nếu cấp phát cho phòng ban dùng chung, không giao đích danh cho cá nhân.
              </small>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Lý do</label>
              <input type="text" className="form-control" name="reason" value={form.reason} onChange={handleChange} disabled={!canCreate} />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Ghi chú</label>
              <textarea className="form-control" rows="3" name="note" value={form.note} onChange={handleChange} disabled={!canCreate} />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Tài sản sẵn sàng cấp phát</label>
              <div className="selection-grid">
                {availableAssets.length ? (
                  availableAssets.map((asset) => (
                    <label key={asset.assetId} className="selection-card">
                      <input
                        type="checkbox"
                        checked={form.assetIds.includes(asset.assetId)}
                        onChange={() => toggleAsset(asset.assetId)}
                        disabled={!canCreate}
                      />
                      <div>
                        <strong>{asset.assetCode}</strong>
                        <span>{asset.assetName}</span>
                      </div>
                    </label>
                  ))
                ) : (
                  <div className="empty-panel">Không có tài sản nào sẵn sàng cấp phát.</div>
                )}
              </div>
            </div>

            {canCreate ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  Tạo phiếu cấp phát
                </button>
              </div>
            ) : null}
          </form>
        </section>

        <section className="content-card content-card--wide">
          <div className="card-head">
            <h3>Danh sách phiếu cấp phát</h3>
            <p>Chọn 1 phiếu để xem chi tiết và thực hiện thao tác tiếp theo.</p>
          </div>

          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã phiếu</th>
                  <th>Ngày</th>
                  <th>Người nhận</th>
                  <th>Trạng thái</th>
                  <th className="text-end">Tác vụ</th>
                </tr>
              </thead>
              <tbody>
                {assignments.length ? (
                  assignments.map((assignment) => (
                    <tr
                      key={assignment.assignmentFormId}
                      onClick={() => handleSelectAssignment(assignment.assignmentFormId)}
                      className="clickable-row"
                    >
                      <td>{assignment.formCode}</td>
                      <td>{formatDate(assignment.assignmentDate)}</td>
                      <td>{assignment.targetUser?.fullName || assignment.targetDepartment?.name || "--"}</td>
                      <td><StatusBadge status={assignment.status} /></td>
                      <td className="text-end">
                        <div className="table-actions">
                          {canApprove && assignment.status === "DRAFT" ? (
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-primary"
                              onClick={(event) => {
                                event.stopPropagation();
                                handleAction(assignment.assignmentFormId, "approve");
                              }}
                            >
                              Phê duyệt
                            </button>
                          ) : null}
                          {canComplete && assignment.status === "CONFIRMED" ? (
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-success"
                              onClick={(event) => {
                                event.stopPropagation();
                                handleAction(assignment.assignmentFormId, "complete");
                              }}
                            >
                              Hoàn tất
                            </button>
                          ) : null}
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      {loading ? "Đang tải..." : "Chưa có phiếu cấp phát."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {selectedAssignment ? (
            <div className="detail-stack mt-4">
              <div className="detail-grid">
                <div>
                  <span className="detail-label">Mã phiếu</span>
                  <strong>{selectedAssignment.formCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Ngày cấp phát</span>
                  <strong>{formatDate(selectedAssignment.assignmentDate)}</strong>
                </div>
                <div>
                  <span className="detail-label">Người lập</span>
                  <strong>{selectedAssignment.issuedByUser?.fullName || "--"}</strong>
                </div>
                <div>
                  <span className="detail-label">Trạng thái</span>
                  <StatusBadge status={selectedAssignment.status} />
                </div>
              </div>

              <div className="selection-grid">
                {selectedAssignment.details?.map((detail) => (
                  <div className="selection-card" key={detail.assignmentFormDetailId}>
                    <div>
                      <strong>{detail.asset?.code}</strong>
                      <span>{detail.asset?.name}</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ) : null}
        </section>
      </div>
    </div>
  );
}

export default AssignmentManagement;
