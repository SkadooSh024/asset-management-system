import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatCard from "../../components/common/StatCard";
import StatusBadge from "../../components/common/StatusBadge";
import { formatDate, formatDateTime, getApiErrorMessage } from "../../utils/format";

function Dashboard() {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchSummary = async () => {
      setLoading(true);
      setError("");

      try {
        const { data } = await axiosClient.get("/api/dashboard/summary");
        setSummary(data);
      } catch (requestError) {
        setError(getApiErrorMessage(requestError, "Khong tai duoc dashboard."));
      } finally {
        setLoading(false);
      }
    };

    fetchSummary();
  }, []);

  return (
    <div className="page-stack">
      <PageHeader
        title="Tong quan he thong"
        description="Theo doi nhanh tai san, su co, cap phat va bao tri de dieu hanh demo nghiep vu."
      />

      {error ? <div className="alert alert-danger">{error}</div> : null}

      <section className="stats-grid">
        <StatCard label="Tong tai san" value={loading ? "..." : summary?.totalAssets || 0} />
        <StatCard label="San sang cap phat" value={loading ? "..." : summary?.readyAssets || 0} tone="success" />
        <StatCard label="Dang su dung" value={loading ? "..." : summary?.assignedAssets || 0} tone="primary" />
        <StatCard label="Su co dang mo" value={loading ? "..." : summary?.openIncidents || 0} tone="warning" />
        <StatCard label="Bao tri dang xu ly" value={loading ? "..." : summary?.activeMaintenanceTickets || 0} tone="danger" />
        <StatCard label="Phieu cap phat cho duyet" value={loading ? "..." : summary?.pendingAssignments || 0} tone="muted" />
      </section>

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Cap phat gan day</h3>
            <p>Cac phieu cap phat moi nhat trong he thong</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma phieu</th>
                  <th>Ngay</th>
                  <th>Nguoi nhan</th>
                  <th>Trang thai</th>
                </tr>
              </thead>
              <tbody>
                {summary?.recentAssignments?.length ? (
                  summary.recentAssignments.map((assignment) => (
                    <tr key={assignment.assignmentFormId}>
                      <td>{assignment.formCode}</td>
                      <td>{formatDate(assignment.assignmentDate)}</td>
                      <td>{assignment.targetUser?.fullName || assignment.targetDepartment?.name || "--"}</td>
                      <td><StatusBadge status={assignment.status} /></td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="4" className="text-center text-muted">
                      Chua co du lieu cap phat.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card">
          <div className="card-head">
            <h3>Su co gan day</h3>
            <p>Cac yeu cau bao hong / ho tro vua duoc ghi nhan</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma bao cao</th>
                  <th>Tieu de</th>
                  <th>Ngay tao</th>
                  <th>Trang thai</th>
                </tr>
              </thead>
              <tbody>
                {summary?.recentIncidents?.length ? (
                  summary.recentIncidents.map((incident) => (
                    <tr key={incident.incidentReportId}>
                      <td>{incident.reportCode}</td>
                      <td>{incident.issueTitle}</td>
                      <td>{formatDateTime(incident.createdAt)}</td>
                      <td><StatusBadge status={incident.status} /></td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="4" className="text-center text-muted">
                      Chua co du lieu su co.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card content-card--full">
          <div className="card-head">
            <h3>Phieu bao tri gan day</h3>
            <p>Theo doi tien do bao tri / sua chua moi nhat</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Ma phieu</th>
                  <th>Tai san</th>
                  <th>Mo luc</th>
                  <th>Uu tien</th>
                  <th>Trang thai</th>
                </tr>
              </thead>
              <tbody>
                {summary?.recentMaintenanceTickets?.length ? (
                  summary.recentMaintenanceTickets.map((ticket) => (
                    <tr key={ticket.maintenanceTicketId}>
                      <td>{ticket.ticketCode}</td>
                      <td>{ticket.asset?.name}</td>
                      <td>{formatDateTime(ticket.createdAt)}</td>
                      <td>{ticket.priority}</td>
                      <td><StatusBadge status={ticket.status} /></td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      Chua co phieu bao tri.
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

export default Dashboard;
