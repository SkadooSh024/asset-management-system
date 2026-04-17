import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import useFeedbackToast from "../../hooks/useFeedbackToast";
import { getStoredUser, hasAnyRole } from "../../utils/auth";
import { formatCurrency, formatDateTime, getApiErrorMessage } from "../../utils/format";
import { ACTION_ACCESS } from "../../config/roleAccess";

const PRIORITY_LABELS = {
  LOW: "Thấp",
  MEDIUM: "Trung bình",
  HIGH: "Cao",
  URGENT: "Khẩn cấp",
};

const MAINTENANCE_TYPE_LABELS = {
  CORRECTIVE: "Khắc phục",
  PREVENTIVE: "Phòng ngừa",
  INSPECTION: "Kiểm tra",
  OTHER: "Khác",
};

const UPDATE_STATUS_LABELS = {
  ASSIGNED: "Đã phân công",
  IN_PROGRESS: "Đang thực hiện",
  WAITING_PARTS: "Chờ linh kiện",
  OUTSOURCED: "Thuê ngoài",
};

function MaintenanceManagement() {
  const user = getStoredUser();
  const [tickets, setTickets] = useState([]);
  const [lookups, setLookups] = useState(null);
  const [incidents, setIncidents] = useState([]);
  const [assets, setAssets] = useState([]);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [createForm, setCreateForm] = useState({
    incidentReportId: "",
    assetId: "",
    assignedToUserId: "",
    priority: "MEDIUM",
    maintenanceType: "CORRECTIVE",
    problemDescription: "",
    externalServiceName: "",
    estimatedCost: "",
  });
  const [updateForm, setUpdateForm] = useState({
    updateStatus: "IN_PROGRESS",
    updateNote: "",
    nextActionDate: "",
    actualCost: "",
    externalServiceName: "",
  });
  const [completeForm, setCompleteForm] = useState({
    resultSummary: "",
    actualCost: "",
    targetStatusId: "",
  });
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useFeedbackToast({ successMessage: message, errorMessage: error });

  const canManage = hasAnyRole(user, ACTION_ACCESS.MAINTENANCE_MANAGE);

  const fetchData = async () => {
    setLoading(true);
    setError("");

    try {
      const [ticketsResponse, lookupsResponse, incidentsResponse, assetsResponse] =
        await Promise.all([
          axiosClient.get("/api/maintenance-tickets"),
          axiosClient.get("/api/lookups/options"),
          axiosClient.get("/api/incidents"),
          axiosClient.get("/api/assets"),
        ]);

      setTickets(ticketsResponse.data);
      setLookups(lookupsResponse.data);
      setIncidents(incidentsResponse.data);
      setAssets(assetsResponse.data);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được dữ liệu bảo trì."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const selectableIncidents = incidents.filter(
    (incident) => incident.status !== "CONVERTED_TO_TICKET" && incident.status !== "RESOLVED"
  );

  const selectTicket = async (ticketId) => {
    try {
      const { data } = await axiosClient.get(`/api/maintenance-tickets/${ticketId}`);
      setSelectedTicket(data);
      setCompleteForm((current) => ({
        ...current,
        targetStatusId: current.targetStatusId || "",
      }));
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tải được chi tiết bảo trì."));
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
      await axiosClient.post("/api/maintenance-tickets", {
        actingUserId: user.userId,
        incidentReportId: createForm.incidentReportId || null,
        assetId: Number(createForm.assetId),
        assignedToUserId: createForm.assignedToUserId
          ? Number(createForm.assignedToUserId)
          : null,
        priority: createForm.priority,
        maintenanceType: createForm.maintenanceType,
        problemDescription: createForm.problemDescription,
        externalServiceName: createForm.externalServiceName,
        estimatedCost: createForm.estimatedCost || null,
      });
      setMessage("Đã tạo phiếu bảo trì.");
      setCreateForm({
        incidentReportId: "",
        assetId: "",
        assignedToUserId: "",
        priority: "MEDIUM",
        maintenanceType: "CORRECTIVE",
        problemDescription: "",
        externalServiceName: "",
        estimatedCost: "",
      });
      fetchData();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không tạo được phiếu bảo trì."));
    }
  };

  const addUpdate = async () => {
    if (!selectedTicket) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await axiosClient.post(`/api/maintenance-tickets/${selectedTicket.maintenanceTicketId}/updates`, {
        actingUserId: user.userId,
        updateStatus: updateForm.updateStatus,
        updateNote: updateForm.updateNote,
        nextActionDate: updateForm.nextActionDate || null,
        actualCost: updateForm.actualCost || null,
        externalServiceName: updateForm.externalServiceName || null,
      });
      setMessage("Đã cập nhật tiến độ bảo trì.");
      setUpdateForm({
        updateStatus: "IN_PROGRESS",
        updateNote: "",
        nextActionDate: "",
        actualCost: "",
        externalServiceName: "",
      });
      fetchData();
      selectTicket(selectedTicket.maintenanceTicketId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không cập nhật được phiếu bảo trì."));
    }
  };

  const completeTicket = async () => {
    if (!selectedTicket) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await axiosClient.patch(`/api/maintenance-tickets/${selectedTicket.maintenanceTicketId}/complete`, {
        actingUserId: user.userId,
        resultSummary: completeForm.resultSummary,
        actualCost: completeForm.actualCost || null,
        targetStatusId: completeForm.targetStatusId ? Number(completeForm.targetStatusId) : null,
      });
      setMessage("Đã hoàn tất bảo trì tài sản.");
      setCompleteForm({
        resultSummary: "",
        actualCost: "",
        targetStatusId: "",
      });
      fetchData();
      selectTicket(selectedTicket.maintenanceTicketId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Không hoàn tất được phiếu bảo trì."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Quản lý bảo trì"
        description="Tạo phiếu bảo trì, cập nhật tiến độ xử lý và chốt kết quả sửa chữa."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Tạo phiếu bảo trì</h3>
            <p>Có thể tạo từ sự cố hoặc tạo trực tiếp cho tài sản.</p>
          </div>

          <form className="form-grid" onSubmit={handleCreate}>
            <div>
              <label className="form-label">Liên kết sự cố</label>
              <select
                className="form-select"
                name="incidentReportId"
                value={createForm.incidentReportId}
                onChange={handleCreateChange}
                disabled={!canManage}
              >
                <option value="">Không liên kết</option>
                {selectableIncidents.map((incident) => (
                  <option key={incident.incidentReportId} value={incident.incidentReportId}>
                    {incident.reportCode} - {incident.issueTitle}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Tài sản</label>
              <select
                className="form-select"
                name="assetId"
                value={createForm.assetId}
                onChange={handleCreateChange}
                required
                disabled={!canManage}
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
              <label className="form-label">Người phụ trách</label>
              <select
                className="form-select"
                name="assignedToUserId"
                value={createForm.assignedToUserId}
                onChange={handleCreateChange}
                disabled={!canManage}
              >
                <option value="">Chưa phân công</option>
                {lookups?.users?.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.fullName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Ưu tiên</label>
              <select className="form-select" name="priority" value={createForm.priority} onChange={handleCreateChange} disabled={!canManage}>
                <option value="LOW">Thấp</option>
                <option value="MEDIUM">Trung bình</option>
                <option value="HIGH">Cao</option>
                <option value="URGENT">Khẩn cấp</option>
              </select>
            </div>

            <div>
              <label className="form-label">Loại bảo trì</label>
              <select className="form-select" name="maintenanceType" value={createForm.maintenanceType} onChange={handleCreateChange} disabled={!canManage}>
                <option value="CORRECTIVE">Khắc phục</option>
                <option value="PREVENTIVE">Phòng ngừa</option>
                <option value="INSPECTION">Kiểm tra</option>
                <option value="OTHER">Khác</option>
              </select>
            </div>

            <div>
              <label className="form-label">Đơn vị sửa chữa ngoài</label>
              <input type="text" className="form-control" name="externalServiceName" value={createForm.externalServiceName} onChange={handleCreateChange} disabled={!canManage} />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mô tả vấn đề</label>
              <textarea className="form-control" rows="4" name="problemDescription" value={createForm.problemDescription} onChange={handleCreateChange} required disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Chi phí dự kiến</label>
              <input type="number" className="form-control" name="estimatedCost" value={createForm.estimatedCost} onChange={handleCreateChange} disabled={!canManage} />
            </div>

            {canManage ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  Tạo phiếu bảo trì
                </button>
              </div>
            ) : null}
          </form>
        </section>

        <section className="content-card content-card--wide">
          <div className="card-head">
            <h3>Danh sách phiếu bảo trì</h3>
            <p>Chọn 1 phiếu để cập nhật tiến độ hoặc chốt kết quả.</p>
          </div>

          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã phiếu</th>
                  <th>Tài sản</th>
                  <th>Ưu tiên</th>
                  <th>Trạng thái</th>
                  <th>Chi phí</th>
                </tr>
              </thead>
              <tbody>
                {tickets.length ? (
                  tickets.map((ticket) => (
                    <tr
                      key={ticket.maintenanceTicketId}
                      onClick={() => selectTicket(ticket.maintenanceTicketId)}
                      className="clickable-row"
                    >
                      <td>{ticket.ticketCode}</td>
                      <td>{ticket.asset?.name}</td>
                      <td>{PRIORITY_LABELS[ticket.priority] || ticket.priority}</td>
                      <td><StatusBadge status={ticket.status} /></td>
                      <td>{formatCurrency(ticket.actualCost || ticket.estimatedCost)}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      {loading ? "Đang tải..." : "Chưa có phiếu bảo trì."}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {selectedTicket ? (
            <div className="detail-stack mt-4">
              <div className="detail-grid">
                <div>
                  <span className="detail-label">Mã phiếu</span>
                  <strong>{selectedTicket.ticketCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Mở lúc</span>
                  <strong>{formatDateTime(selectedTicket.createdAt)}</strong>
                </div>
                <div>
                  <span className="detail-label">Trạng thái</span>
                  <StatusBadge status={selectedTicket.status} />
                </div>
                <div>
                  <span className="detail-label">Chi phí hiện tại</span>
                  <strong>{formatCurrency(selectedTicket.actualCost || selectedTicket.estimatedCost)}</strong>
                </div>
                <div>
                  <span className="detail-label">Loại bảo trì</span>
                  <strong>{MAINTENANCE_TYPE_LABELS[selectedTicket.maintenanceType] || selectedTicket.maintenanceType}</strong>
                </div>
                <div>
                  <span className="detail-label">Ưu tiên</span>
                  <strong>{PRIORITY_LABELS[selectedTicket.priority] || selectedTicket.priority}</strong>
                </div>
              </div>

              {canManage ? (
                <div className="content-grid">
                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Cập nhật tiến độ</h3>
                    </div>
                    <div className="form-grid">
                      <select
                        className="form-select"
                        value={updateForm.updateStatus}
                        onChange={(event) =>
                          setUpdateForm((current) => ({
                            ...current,
                            updateStatus: event.target.value,
                          }))
                        }
                      >
                        <option value="ASSIGNED">Đã phân công</option>
                        <option value="IN_PROGRESS">Đang thực hiện</option>
                        <option value="WAITING_PARTS">Chờ linh kiện</option>
                        <option value="OUTSOURCED">Thuê ngoài</option>
                      </select>
                      <textarea
                        className="form-control"
                        rows="3"
                        value={updateForm.updateNote}
                        onChange={(event) =>
                          setUpdateForm((current) => ({
                            ...current,
                            updateNote: event.target.value,
                          }))
                        }
                        placeholder="Nội dung cập nhật"
                      />
                      <input
                        type="date"
                        className="form-control"
                        value={updateForm.nextActionDate}
                        onChange={(event) =>
                          setUpdateForm((current) => ({
                            ...current,
                            nextActionDate: event.target.value,
                          }))
                        }
                      />
                      <input
                        type="number"
                        className="form-control"
                        placeholder="Chi phí thực tế phát sinh"
                        value={updateForm.actualCost}
                        onChange={(event) =>
                          setUpdateForm((current) => ({
                            ...current,
                            actualCost: event.target.value,
                          }))
                        }
                      />
                      <input
                        type="text"
                        className="form-control"
                        placeholder="Đơn vị sửa chữa ngoài"
                        value={updateForm.externalServiceName}
                        onChange={(event) =>
                          setUpdateForm((current) => ({
                            ...current,
                            externalServiceName: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-primary" onClick={addUpdate}>
                        Lưu cập nhật
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Hoàn tất bảo trì</h3>
                    </div>
                    <div className="form-grid">
                      <textarea
                        className="form-control"
                        rows="3"
                        value={completeForm.resultSummary}
                        onChange={(event) =>
                          setCompleteForm((current) => ({
                            ...current,
                            resultSummary: event.target.value,
                          }))
                        }
                        placeholder="Kết quả sửa chữa"
                      />
                      <input
                        type="number"
                        className="form-control"
                        placeholder="Tổng chi phí thực tế"
                        value={completeForm.actualCost}
                        onChange={(event) =>
                          setCompleteForm((current) => ({
                            ...current,
                            actualCost: event.target.value,
                          }))
                        }
                      />
                      <select
                        className="form-select"
                        value={completeForm.targetStatusId}
                        onChange={(event) =>
                          setCompleteForm((current) => ({
                            ...current,
                            targetStatusId: event.target.value,
                          }))
                        }
                      >
                        <option value="">Mặc định về sẵn sàng</option>
                        {lookups?.assetStatuses?.map((status) => (
                          <option key={status.id} value={status.id}>
                            {status.name}
                          </option>
                        ))}
                      </select>
                      <button type="button" className="btn btn-outline-success" onClick={completeTicket}>
                        Hoàn tất bảo trì
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Lịch sử cập nhật</h3>
                    </div>
                    <div className="activity-list">
                      {selectedTicket.updates?.length ? (
                        selectedTicket.updates.map((update) => (
                          <article key={update.maintenanceUpdateId} className="activity-item">
                            <strong>{UPDATE_STATUS_LABELS[update.updateStatus] || update.updateStatus || "Cập nhật"}</strong>
                            <p>{update.updateNote}</p>
                            <span>{formatDateTime(update.updateTime)}</span>
                          </article>
                        ))
                      ) : (
                        <div className="empty-panel">Chưa có cập nhật tiến độ nào.</div>
                      )}
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

export default MaintenanceManagement;
