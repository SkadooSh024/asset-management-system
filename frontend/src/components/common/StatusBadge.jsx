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

function StatusBadge({ status }) {
  const tone = TONE_BY_STATUS[status] || "muted";

  return <span className={`status-badge tone-${tone}`}>{status || "--"}</span>;
}

export default StatusBadge;
