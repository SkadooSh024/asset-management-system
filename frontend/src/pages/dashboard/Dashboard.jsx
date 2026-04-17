import { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import PageHeader from "../../components/common/PageHeader";
import StatCard from "../../components/common/StatCard";
import StatusBadge from "../../components/common/StatusBadge";
import { formatDate, formatDateTime, getApiErrorMessage } from "../../utils/format";

const PRIORITY_LABELS = {
  LOW: "Thấp",
  MEDIUM: "Trung bình",
  HIGH: "Cao",
  URGENT: "Khẩn cấp",
};

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
        setError(getApiErrorMessage(requestError, "Không tải được bảng tổng quan."));
      } finally {
        setLoading(false);
      }
    };

    fetchSummary();
  }, []);

  return (
    <div className="page-stack">
      <PageHeader
        title="Tổng quan hệ thống"
        description="Theo dõi nhanh tài sản, sự cố, cấp phát và bảo trì để điều hành demo nghiệp vụ."
      />

      {error ? <div className="alert alert-danger">{error}</div> : null}

      <section className="stats-grid">
        <StatCard label="Tổng tài sản" value={loading ? "..." : summary?.totalAssets || 0} />
        <StatCard label="Sẵn sàng cấp phát" value={loading ? "..." : summary?.readyAssets || 0} tone="success" />
        <StatCard label="Đang sử dụng" value={loading ? "..." : summary?.assignedAssets || 0} tone="primary" />
        <StatCard label="Sự cố đang mở" value={loading ? "..." : summary?.openIncidents || 0} tone="warning" />
        <StatCard label="Bảo trì đang xử lý" value={loading ? "..." : summary?.activeMaintenanceTickets || 0} tone="danger" />
        <StatCard label="Phiếu cấp phát chờ duyệt" value={loading ? "..." : summary?.pendingAssignments || 0} tone="muted" />
      </section>

      <div className="content-grid">
        <section className="content-card">
          <div className="card-head">
            <h3>Cấp phát gần đây</h3>
            <p>Các phiếu cấp phát mới nhất trong hệ thống</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã phiếu</th>
                  <th>Ngày</th>
                  <th>Người nhận</th>
                  <th>Trạng thái</th>
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
                      Chưa có dữ liệu cấp phát.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card">
          <div className="card-head">
            <h3>Sự cố gần đây</h3>
            <p>Các yêu cầu báo hỏng hoặc hỗ trợ vừa được ghi nhận</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã báo cáo</th>
                  <th>Tiêu đề</th>
                  <th>Ngày tạo</th>
                  <th>Trạng thái</th>
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
                      Chưa có dữ liệu sự cố.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="content-card content-card--full">
          <div className="card-head">
            <h3>Phiếu bảo trì gần đây</h3>
            <p>Theo dõi tiến độ bảo trì hoặc sửa chữa mới nhất</p>
          </div>
          <div className="table-responsive">
            <table className="table align-middle custom-table">
              <thead>
                <tr>
                  <th>Mã phiếu</th>
                  <th>Tài sản</th>
                  <th>Mở lúc</th>
                  <th>Ưu tiên</th>
                  <th>Trạng thái</th>
                </tr>
              </thead>
              <tbody>
                {summary?.recentMaintenanceTickets?.length ? (
                  summary.recentMaintenanceTickets.map((ticket) => (
                    <tr key={ticket.maintenanceTicketId}>
                      <td>{ticket.ticketCode}</td>
                      <td>{ticket.asset?.name}</td>
                      <td>{formatDateTime(ticket.createdAt)}</td>
                      <td>{PRIORITY_LABELS[ticket.priority] || ticket.priority}</td>
                      <td><StatusBadge status={ticket.status} /></td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="5" className="text-center text-muted">
                      Chưa có phiếu bảo trì.
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
