import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import useFeedbackToast from "../../hooks/useFeedbackToast";
import { getStoredUser, hasAnyRole } from "../../utils/auth";
import { formatDateTime, getApiErrorMessage } from "../../utils/format";
import { ACTION_ACCESS } from "../../config/roleAccess";

const SEVERITY_LABELS = {
  LOW: "Thấp",
  MEDIUM: "Trung bình",
  HIGH: "Cao",
  CRITICAL: "Nghiêm trọng",
};

function IncidentManagement() {
  const user = getStoredUser();
  const [incidents, setIncidents] = useState([]);
  const [assets, setAssets] = useState([]);
  const [lookups, setLookups] = useState(null);
  const [selectedIncident, setSelectedIncident] = useState(null);
  const [createForm, setCreateForm] = useState({
    assetId: "",
    severity: "MEDIUM",
    issueTitle: "",
    issueDescription: "",
  });
  const [assignForm, setAssignForm] = useState({ assignedToUserId: "", note: "" });
  const [convertForm, setConvertForm] = useState({
    assignedToUserId: "",
    priority: "MEDIUM",
    maintenanceType: "CORRECTIVE",
    problemDescription: "",
    estimatedCost: "",
  });
  const [closeForm, setCloseForm] = useState({
    status: "REJECTED",
    resolutionNote: "",
  });
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useFeedbackToast({ successMessage: message, errorMessage: error });

  const canReport = hasAnyRole(user, ACTION_ACCESS.INCIDENT_REPORT);
  const canHandle = hasAnyRole(user, ACTION_ACCESS.INCIDENT_HANDLE);

  const fetchData = async () => {
    setLoading(true);
    setError("");

    try {
      const [incidentsResponse, assetsResponse, lookupsResponse] = await Promise.all([
        axiosClient.get("/api/incidents"),
        axiosClient.get("/api/assets"),
        axiosClient.get("/api/lookups/options"),
      ]);

      setIncidents(incidentsResponse.data);
      setAssets(assetsResponse.data);
      setLookups(lookupsResponse.data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được dữ liệu sự cố."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const selectIncident = async (incidentId) => {
    try {
      const { data } = await axiosClient.get(`/api/incidents/${incidentId}`);
      setSelectedIncident(data);
      setAssignForm({ assignedToUserId: data.assignedToUser?.id || "", note: "" });
      setConvertForm({
        assignedToUserId: data.assignedToUser?.id || "",
        priority: "MEDIUM",
        maintenanceType: "CORRECTIVE",
        problemDescription: data.issueDescription || "",
        estimatedCost: "",
      });
      setCloseForm({ status: "REJECTED", resolutionNote: "" });
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được chi tiết sự cố."));
    }
  };

  const handleCreateChange = (event) => {
    const { name, value } = event.target;
    setCreateForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const handleCreate = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      await axiosClient.post("/api/incidents", {
        assetId: Number(createForm.assetId),
        reportedByUserId: user.userId,
        severity: createForm.severity,
        issueTitle: createForm.issueTitle,
        issueDescription: createForm.issueDescription,
      });
      setMessage("Đã ghi nhận báo hỏng hoặc sự cố.");
      setCreateForm({
        assetId: "",
        severity: "MEDIUM",
        issueTitle: "",
        issueDescription: "",
      });
      fetchData();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tạo được báo hỏng hoặc sự cố."));
    }
  };

  const handleAssign = async () => {
    if (!selectedIncident) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/incidents/${selectedIncident.incidentReportId}/assign`, {
        actingUserId: user.userId,
        assignedToUserId: Number(assignForm.assignedToUserId),
        note: assignForm.note,
      });
      setMessage("Đã phân công xử lý sự cố.");
      fetchData();
      selectIncident(selectedIncident.incidentReportId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không thể phân công xử lý."));
    }
  };

  const handleConvert = async () => {
    if (!selectedIncident) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await axiosClient.post(
        `/api/incidents/${selectedIncident.incidentReportId}/convert-to-maintenance`,
        {
          actingUserId: user.userId,
          assignedToUserId: convertForm.assignedToUserId
            ? Number(convertForm.assignedToUserId)
            : null,
          priority: convertForm.priority,
          maintenanceType: convertForm.maintenanceType,
          problemDescription: convertForm.problemDescription,
          estimatedCost: convertForm.estimatedCost || null,
        }
      );
      setMessage("Đã chuyển sự cố thành phiếu bảo trì.");
      fetchData();
      selectIncident(selectedIncident.incidentReportId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không thể chuyển thành phiếu bảo trì."));
    }
  };

  const handleClose = async () => {
    if (!selectedIncident) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/incidents/${selectedIncident.incidentReportId}/close`, {
        actingUserId: user.userId,
        status: closeForm.status,
        resolutionNote: closeForm.resolutionNote,
      });
      setMessage("Đã cập nhật kết quả xử lý sự cố.");
      fetchData();
      selectIncident(selectedIncident.incidentReportId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không thể đóng hoặc cập nhật sự cố."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Báo hỏng hoặc sự cố tài sản"
        description="Ghi nhận, phân công xử lý và chuyển sự cố sang bảo trì khi cần."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Ghi nhận sự cố</h3>
            <p>Tài khoản hiện tại có thể gửi yêu cầu hỗ trợ cho tài sản gặp vấn đề.</p>
          </div>

          <form className="form-grid" onSubmit={handleCreate}>
            <div>
              <label className="form-label">Tài sản</label>
              <select
                className="form-select"
                name="assetId"
                value={createForm.assetId}
                onChange={handleCreateChange}
                required
                disabled={!canReport}
              >
                <option value="">Chọn tài sản</option>
                {assets.map((asset) => (
                  <option key={asset.assetId} value={asset.assetId}>
                    {asset.assetCode} - {asset.assetName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Mức độ</label>
              <select
                className="form-select"
                name="severity"
                value={createForm.severity}
                onChange={handleCreateChange}
                disabled={!canReport}
              >
                <option value="LOW">Thấp</option>
                <option value="MEDIUM">Trung bình</option>
                <option value="HIGH">Cao</option>
                <option value="CRITICAL">Nghiêm trọng</option>
              </select>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Tiêu đề</label>
              <input
                type="text"
                className="form-control"
                name="issueTitle"
                value={createForm.issueTitle}
                onChange={handleCreateChange}
                required
                disabled={!canReport}
              />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mô tả chi tiết</label>
              <textarea
                className="form-control"
                rows="4"
                name="issueDescription"
                value={createForm.issueDescription}
                onChange={handleCreateChange}
                required
                disabled={!canReport}
              />
            </div>

            {canReport ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  Gửi báo hỏng hoặc sự cố
                </button>
              </div>
            ) : null}
          </form>
        </section>

        <section className="content-card content-card--wide">
          <div className="card-head">
            <h3>Danh sách sự cố</h3>
            <p>Chọn 1 sự cố để xử lý tiếp nếu tài khoản có quyền.</p>
          </div>

          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã báo cáo</th>
                  <th>Tiêu đề</th>
                  <th>Tài sản</th>
                  <th>Mức độ</th>
                  <th>Trạng thái</th>
                </tr>
              </thead>
              <tbody>
                {incidents.length ? (
                  incidents.map((incident) => (
                    <tr
                      key={incident.incidentReportId}
                      onClick={() => selectIncident(incident.incidentReportId)}
                      className="clickable-row"
                    >
                      <td>{incident.reportCode}</td>
                      <td>{incident.issueTitle}</td>
                      <td>{incident.asset?.name}</td>
                      <td>{SEVERITY_LABELS[incident.severity] || incident.severity}</td>
                      <td><StatusBadge status={incident.status} /></td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      {loading ? "Đang tải..." : "Chưa có sự cố nào được ghi nhận."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {selectedIncident ? (
            <div className="detail-stack mt-4">
              <div className="detail-grid">
                <div>
                  <span className="detail-label">Mã báo cáo</span>
                  <strong>{selectedIncident.reportCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Tạo lúc</span>
                  <strong>{formatDateTime(selectedIncident.createdAt)}</strong>
                </div>
                <div>
                  <span className="detail-label">Trạng thái</span>
                  <StatusBadge status={selectedIncident.status} />
                </div>
                <div>
                  <span className="detail-label">Người báo cáo</span>
                  <strong>{selectedIncident.reportedByUser?.fullName}</strong>
                </div>
              </div>

              {canHandle ? (
                <div className="content-grid">
                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Phân công xử lý</h3>
                    </div>
                    <div className="form-grid">
                      <select
                        className="form-select"
                        value={assignForm.assignedToUserId}
                        onChange={(event) =>
                          setAssignForm((current) => ({
                            ...current,
                            assignedToUserId: event.target.value,
                          }))
                        }
                      >
                        <option value="">Chọn người xử lý</option>
                        {lookups?.users?.map((item) => (
                          <option key={item.id} value={item.id}>
                            {item.fullName}
                          </option>
                        ))}
                      </select>
                      <textarea
                        className="form-control"
                        rows="3"
                        placeholder="Ghi chú phân công"
                        value={assignForm.note}
                        onChange={(event) =>
                          setAssignForm((current) => ({
                            ...current,
                            note: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-primary" onClick={handleAssign}>
                        Phân công
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Chuyển thành bảo trì</h3>
                    </div>
                    <div className="form-grid">
                      <select
                        className="form-select"
                        value={convertForm.assignedToUserId}
                        onChange={(event) =>
                          setConvertForm((current) => ({
                            ...current,
                            assignedToUserId: event.target.value,
                          }))
                        }
                      >
                        <option value="">Chọn người phụ trách</option>
                        {lookups?.users?.map((item) => (
                          <option key={item.id} value={item.id}>
                            {item.fullName}
                          </option>
                        ))}
                      </select>
                      <select
                        className="form-select"
                        value={convertForm.priority}
                        onChange={(event) =>
                          setConvertForm((current) => ({
                            ...current,
                            priority: event.target.value,
                          }))
                        }
                      >
                        <option value="LOW">Thấp</option>
                        <option value="MEDIUM">Trung bình</option>
                        <option value="HIGH">Cao</option>
                        <option value="URGENT">Khẩn cấp</option>
                      </select>
                      <select
                        className="form-select"
                        value={convertForm.maintenanceType}
                        onChange={(event) =>
                          setConvertForm((current) => ({
                            ...current,
                            maintenanceType: event.target.value,
                          }))
                        }
                      >
                        <option value="CORRECTIVE">Khắc phục</option>
                        <option value="PREVENTIVE">Phòng ngừa</option>
                        <option value="INSPECTION">Kiểm tra</option>
                        <option value="OTHER">Khác</option>
                      </select>
                      <textarea
                        className="form-control"
                        rows="3"
                        value={convertForm.problemDescription}
                        onChange={(event) =>
                          setConvertForm((current) => ({
                            ...current,
                            problemDescription: event.target.value,
                          }))
                        }
                      />
                      <input
                        type="number"
                        className="form-control"
                        placeholder="Chi phí dự kiến"
                        value={convertForm.estimatedCost}
                        onChange={(event) =>
                          setConvertForm((current) => ({
                            ...current,
                            estimatedCost: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-danger" onClick={handleConvert}>
                        Chuyển sang bảo trì
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Đóng hoặc từ chối yêu cầu</h3>
                    </div>
                    <div className="form-grid">
                      <select
                        className="form-select"
                        value={closeForm.status}
                        onChange={(event) =>
                          setCloseForm((current) => ({
                            ...current,
                            status: event.target.value,
                          }))
                        }
                      >
                        <option value="REJECTED">Từ chối</option>
                        <option value="CANCELED">Đã hủy</option>
                        <option value="RESOLVED">Đã xử lý</option>
                      </select>
                      <textarea
                        className="form-control"
                        rows="3"
                        placeholder="Mô tả kết quả xử lý"
                        value={closeForm.resolutionNote}
                        onChange={(event) =>
                          setCloseForm((current) => ({
                            ...current,
                            resolutionNote: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-secondary" onClick={handleClose}>
                        Cập nhật trạng thái
                      </button>
                    </div>
                  </section>
                </div>
              ) : null}
            </div>
          ) : null}
        </section>
      </div>
    </div>
  );
}

export default IncidentManagement;
