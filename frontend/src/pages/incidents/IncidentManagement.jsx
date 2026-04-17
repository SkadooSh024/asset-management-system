import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatusBadge from "../../components/common/StatusBadge";
import { getStoredUser, hasAnyRole } from "../../utils/auth";
import { formatDateTime, getApiErrorMessage } from "../../utils/format";

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

  const canHandle = hasAnyRole(user, ["ADMIN", "ASSET_STAFF", "MANAGER"]);

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
      setError(getApiErrorMessage(requestError, "Khong tai duoc du lieu su co."));
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
      setError(getApiErrorMessage(requestError, "Khong tai duoc chi tiet su co."));
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
      setMessage("Da ghi nhan bao hong / su co.");
      setCreateForm({
        assetId: "",
        severity: "MEDIUM",
        issueTitle: "",
        issueDescription: "",
      });
      fetchData();
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong tao duoc bao hong / su co."));
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
      setMessage("Da phan cong xu ly su co.");
      fetchData();
      selectIncident(selectedIncident.incidentReportId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong the phan cong xu ly."));
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
      setMessage("Da chuyen su co thanh phieu bao tri.");
      fetchData();
      selectIncident(selectedIncident.incidentReportId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong the chuyen thanh phieu bao tri."));
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
      setMessage("Da cap nhat ket qua xu ly su co.");
      fetchData();
      selectIncident(selectedIncident.incidentReportId);
    } catch (requestError) {
      setError(getApiErrorMessage(requestError, "Khong the dong / cap nhat su co."));
    }
  };

  return (
    <div className="page-stack">
      <PageHeader
        title="Bao hong / su co tai san"
        description="Ghi nhan, phan cong xu ly va chuyen su co sang bao tri khi can."
      />

      {message ? <div className="alert alert-success">{message}</div> : null}
      {error ? <div className="alert alert-danger">{error}</div> : null}

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Ghi nhan su co</h3>
            <p>Tai khoan hien tai co the gui yeu cau ho tro cho tai san gap van de.</p>
          </div>

          <form className="form-grid" onSubmit={handleCreate}>
            <div>
              <label className="form-label">Tai san</label>
              <select
                className="form-select"
                name="assetId"
                value={createForm.assetId}
                onChange={handleCreateChange}
                required
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
              <label className="form-label">Muc do</label>
              <select
                className="form-select"
                name="severity"
                value={createForm.severity}
                onChange={handleCreateChange}
              >
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
                <option value="CRITICAL">CRITICAL</option>
              </select>
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Tieu de</label>
              <input
                type="text"
                className="form-control"
                name="issueTitle"
                value={createForm.issueTitle}
                onChange={handleCreateChange}
                required
              />
            </div>

            <div className="form-grid form-grid--full">
              <label className="form-label">Mo ta chi tiet</label>
              <textarea
                className="form-control"
                rows="4"
                name="issueDescription"
                value={createForm.issueDescription}
                onChange={handleCreateChange}
                required
              />
            </div>

            <div className="form-actions">
              <button type="submit" className="btn btn-primary">
                Gui bao hong / su co
              </button>
            </div>
          </form>
        </section>

        <section className="content-card content-card--wide">
          <div className="card-head">
            <h3>Danh sach su co</h3>
            <p>Chon 1 su co de xu ly tiep neu tai khoan co quyen.</p>
          </div>

          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma bao cao</th>
                  <th>Tieu de</th>
                  <th>Tai san</th>
                  <th>Muc do</th>
                  <th>Trang thai</th>
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
                      <td>{incident.severity}</td>
                      <td><StatusBadge status={incident.status} /></td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      {loading ? "Dang tai..." : "Chua co su co nao duoc ghi nhan."}
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
                  <span className="detail-label">Ma bao cao</span>
                  <strong>{selectedIncident.reportCode}</strong>
                </div>
                <div>
                  <span className="detail-label">Tao luc</span>
                  <strong>{formatDateTime(selectedIncident.createdAt)}</strong>
                </div>
                <div>
                  <span className="detail-label">Trang thai</span>
                  <StatusBadge status={selectedIncident.status} />
                </div>
                <div>
                  <span className="detail-label">Nguoi bao cao</span>
                  <strong>{selectedIncident.reportedByUser?.fullName}</strong>
                </div>
              </div>

              {canHandle ? (
                <div className="content-grid">
                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Phan cong xu ly</h3>
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
                        <option value="">Chon nguoi xu ly</option>
                        {lookups?.users?.map((item) => (
                          <option key={item.id} value={item.id}>
                            {item.fullName}
                          </option>
                        ))}
                      </select>
                      <textarea
                        className="form-control"
                        rows="3"
                        placeholder="Ghi chu phan cong"
                        value={assignForm.note}
                        onChange={(event) =>
                          setAssignForm((current) => ({
                            ...current,
                            note: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-primary" onClick={handleAssign}>
                        Phan cong
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Chuyen thanh bao tri</h3>
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
                        <option value="">Chon nguoi phu trach</option>
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
                        <option value="LOW">LOW</option>
                        <option value="MEDIUM">MEDIUM</option>
                        <option value="HIGH">HIGH</option>
                        <option value="URGENT">URGENT</option>
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
                        <option value="CORRECTIVE">CORRECTIVE</option>
                        <option value="PREVENTIVE">PREVENTIVE</option>
                        <option value="INSPECTION">INSPECTION</option>
                        <option value="OTHER">OTHER</option>
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
                        placeholder="Chi phi du kien"
                        value={convertForm.estimatedCost}
                        onChange={(event) =>
                          setConvertForm((current) => ({
                            ...current,
                            estimatedCost: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-danger" onClick={handleConvert}>
                        Chuyen sang bao tri
                      </button>
                    </div>
                  </section>

                  <section className="content-card nested-card">
                    <div className="card-head">
                      <h3>Dong / tu choi yeu cau</h3>
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
                        <option value="REJECTED">REJECTED</option>
                        <option value="CANCELED">CANCELED</option>
                        <option value="RESOLVED">RESOLVED</option>
                      </select>
                      <textarea
                        className="form-control"
                        rows="3"
                        placeholder="Mo ta ket qua xu ly"
                        value={closeForm.resolutionNote}
                        onChange={(event) =>
                          setCloseForm((current) => ({
                            ...current,
                            resolutionNote: event.target.value,
                          }))
                        }
                      />
                      <button type="button" className="btn btn-outline-secondary" onClick={handleClose}>
                        Cap nhat trang thai
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
