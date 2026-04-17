import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import { getStoredUser, hasAnyRole } from "../../utils/auth";
import { formatCurrency, formatDateTime, getApiErrorMessage } from "../../utils/format";

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

  const canManage = hasAnyRole(user, ["ADMIN", "ASSET_STAFF"]);

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
      setError(getApiErrorMessage(requestError, "Khong tai duoc du lieu bao tri."));
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
      setError(getApiErrorMessage(requestError, "Khong tai duoc chi tiet bao tri."));
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
      setMessage("Da tao phieu bao tri.");
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
      setError(getApiErrorMessage(requestError, "Khong tao duoc phieu bao tri."));
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
      setMessage("Da cap nhat tien do bao tri.");
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
      setError(getApiErrorMessage(requestError, "Khong cap nhat duoc phieu bao tri."));
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
      setMessage("Da hoan tat bao tri tai san.");
      setCompleteForm({
        resultSummary: "",
        actualCost: "",
        targetStatusId: "",
      });
      fetchData();
      selectTicket(selectedTicket.maintenanceTicketId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong hoan tat duoc phieu bao tri."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Quan ly bao tri"
        description="Tao phieu bao tri, cap nhat tien do xu ly va chot ket qua sua chua."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Tao phieu bao tri</h3>
            <p>Co the tao tu su co hoac tao truc tiep cho tai san.</p>
          </div>

          <form className="form-grid" onSubmit={handleCreate}>
            <div>
              <label className="form-label">Lien ket su co</label>
              <select
                className="form-select"
                name="incidentReportId"
                value={createForm.incidentReportId}
                onChange={handleCreateChange}
                disabled={!canManage}
              >
                <option value="">Khong lien ket</option>
                {selectableIncidents.map((incident) => (
                  <option key={incident.incidentReportId} value={incident.incidentReportId}>
                    {incident.reportCode} - {incident.issueTitle}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Tai san</label>
              <select
                className="form-select"
                name="assetId"
                value={createForm.assetId}
                onChange={handleCreateChange}
                required
                disabled={!canManage}
              >
                <option value="">Chon tai san</option>
                {assets.map((asset) => (
                  <option key={asset.assetId} value={asset.assetId}>
                    {asset.assetCode} - {asset.assetName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Nguoi phu trach</label>
              <select
                className="form-select"
                name="assignedToUserId"
                value={createForm.assignedToUserId}
                onChange={handleCreateChange}
                disabled={!canManage}
              >
                <option value="">Chua phan cong</option>
                {lookups?.users?.map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.fullName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="form-label">Uu tien</label>
              <select className="form-select" name="priority" value={createForm.priority} onChange={handleCreateChange} disabled={!canManage}>
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
                <option value="URGENT">URGENT</option>
              </select>
            </div>

            <div>
              <label className="form-label">Loai bao tri</label>
              <select className="form-select" name="maintenanceType" value={createForm.maintenanceType} onChange={handleCreateChange} disabled={!canManage}>
                <option value="CORRECTIVE">CORRECTIVE</option>
                <option value="PREVENTIVE">PREVENTIVE</option>
                <option value="INSPECTION">INSPECTION</option>
                <option value="OTHER">OTHER</option>
              </select>
            </div>

            <div>
              <label className="form-label">Don vi sua chua ngoai</label>
              <input type="text" className="form-control" name="externalServiceName" value={createForm.externalServiceName} onChange={handleCreateChange} disabled={!canManage} />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mo ta van de</label>
              <textarea className="form-control" rows="4" name="problemDescription" value={createForm.problemDescription} onChange={handleCreateChange} required disabled={!canManage} />
            </div>

            <div>
              <label className="form-label">Chi phi du kien</label>
              <input type="number" className="form-control" name="estimatedCost" value={createForm.estimatedCost} onChange={handleCreateChange} disabled={!canManage} />
            </div>

            {canManage ? (
              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  Tao phieu bao tri
                </button>
              </div>
            ) : null}
          </form>
        </section>

        <section className="content-card content-card--wide">
          <div className="card-head">
            <h3>Danh sach phieu bao tri</h3>
            <p>Chon 1 phieu de cap nhat tien do hoac chot ket qua.</p>
          </div>

          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma phieu</th>
                  <th>Tai san</th>
                  <th>Uu tien</th>
                  <th>Trang thai</th>
                  <th>Chi phi</th>
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
                      <td>{ticket.priority}</td>
                      <td><StatusBadge status={ticket.status} /></td>
                      <td>{formatCurrency(ticket.actualCost || ticket.estimatedCost)}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      {loading ? "Dang tai..." : "Chua co phieu bao tri."}
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
                  <span className="detail-label">Ma phieu</span>
                  <strong>{selectedTicket.ticketCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Mo luc</span>
                  <strong>{formatDateTime(selectedTicket.createdAt)}</strong>
                </div>
                <div>
                  <span className="detail-label">Trang thai</span>
                  <StatusBadge status={selectedTicket.status} />
                </div>
                <div>
                  <span className="detail-label">Chi phi hien tai</span>
                  <strong>{formatCurrency(selectedTicket.actualCost || selectedTicket.estimatedCost)}</strong>
                </div>
              </div>

              {canManage ? (
                <div className="content-grid">
                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Cap nhat tien do</h3>
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
                        <option value="ASSIGNED">ASSIGNED</option>
                        <option value="IN_PROGRESS">IN_PROGRESS</option>
                        <option value="WAITING_PARTS">WAITING_PARTS</option>
                        <option value="OUTSOURCED">OUTSOURCED</option>
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
                        placeholder="Noi dung cap nhat"
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
                        placeholder="Chi phi thuc te phat sinh"
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
                        placeholder="Don vi sua chua ngoai"
                        value={updateForm.externalServiceName}
                        onChange={(event) =>
                          setUpdateForm((current) => ({
                            ...current,
                            externalServiceName: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-primary" onClick={addUpdate}>
                        Luu cap nhat
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Hoan tat bao tri</h3>
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
                        placeholder="Ket qua sua chua"
                      />
                      <input
                        type="number"
                        className="form-control"
                        placeholder="Tong chi phi thuc te"
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
                        <option value="">Mac dinh ve READY</option>
                        {lookups?.assetStatuses?.map((status) => (
                          <option key={status.id} value={status.id}>
                            {status.name}
                          </option>
                        ))}
                      </select>
                      <button type="button" className="btn btn-outline-success" onClick={completeTicket}>
                        Hoan tat bao tri
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Lich su cap nhat</h3>
                    </div>
                    <div className="activity-list">
                      {selectedTicket.updates?.length ? (
                        selectedTicket.updates.map((update) => (
                          <article key={update.maintenanceUpdateId} className="activity-item">
                            <strong>{update.updateStatus || "UPDATE"}</strong>
                            <p>{update.updateNote}</p>
                            <span>{formatDateTime(update.updateTime)}</span>
                          </article>
                        ))
                      ) : (
                        <div className="empty-panel">Chua co cap nhat tien do nao.</div>
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
