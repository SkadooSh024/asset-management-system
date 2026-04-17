const TONE_BY_STATUS = {
  ACTIVE: "success",
  READY: "success",
  COMPLETED: "success",
  RESOLVED: "success",
  IN_USE: "primary",
  CONFIRMED: "primary",
  IN_REVIEW: "warning",
  OPEN: "warning",
  DRAFT: "muted",
  WAITING_MAINTENANCE: "danger",
  IN_MAINTENANCE: "danger",
  CANCELED: "muted",
  REJECTED: "muted",
  RETIRED: "muted",
};

const LABEL_BY_STATUS = {
  ACTIVE: "Hoạt động",
  READY: "Sẵn sàng",
  COMPLETED: "Hoàn tất",
  RESOLVED: "Đã xử lý",
  IN_USE: "Đang sử dụng",
  CONFIRMED: "Đã duyệt",
  IN_REVIEW: "Đang xử lý",
  OPEN: "Mới tạo",
  DRAFT: "Nháp",
  WAITING_MAINTENANCE: "Chờ bảo trì",
  IN_MAINTENANCE: "Đang bảo trì",
  CANCELED: "Đã hủy",
  REJECTED: "Từ chối",
  RETIRED: "Thanh lý",
  ASSIGNED: "Đã phân công",
  WAITING_PARTS: "Chờ linh kiện",
  OUTSOURCED: "Thuê ngoài",
  CONVERTED_TO_TICKET: "Đã chuyển bảo trì",
  INACTIVE: "Ngừng hoạt động",
};

function StatusBadge({ status }) {
  const tone = TONE_BY_STATUS[status] || "muted";
  const label = LABEL_BY_STATUS[status] || status || "--";

  return <span className={`status-badge tone-${tone}`}>{label}</span>;
}

export default StatusBadge;
