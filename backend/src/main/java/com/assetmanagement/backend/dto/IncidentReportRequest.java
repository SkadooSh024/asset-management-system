package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class IncidentReportRequest {

    @NotNull(message = "Tài sản không được để trống.")
        private Long assetId;

        @NotNull(message = "Người báo hỏng không được để trống.")
        private Long reportedByUserId;

        private Long assignedToUserId;

        @NotBlank(message = "Mức độ sự cố không được để trống.")
        private String severity;

        @NotBlank(message = "Tiêu đề sự cố không được để trống.")
        private String issueTitle;

        @NotBlank(message = "Mô tả sự cố không được để trống.")
        private String issueDescription;

    public Long getAssetId() {
            return assetId;
        }

    public void setAssetId(Long assetId) {
            this.assetId = assetId;
        }

    public Long getReportedByUserId() {
            return reportedByUserId;
        }

    public void setReportedByUserId(Long reportedByUserId) {
            this.reportedByUserId = reportedByUserId;
        }

    public Long getAssignedToUserId() {
            return assignedToUserId;
        }

    public void setAssignedToUserId(Long assignedToUserId) {
            this.assignedToUserId = assignedToUserId;
        }

    public String getSeverity() {
            return severity;
        }

    public void setSeverity(String severity) {
            this.severity = severity;
        }

    public String getIssueTitle() {
            return issueTitle;
        }

    public void setIssueTitle(String issueTitle) {
            this.issueTitle = issueTitle;
        }

    public String getIssueDescription() {
            return issueDescription;
        }

    public void setIssueDescription(String issueDescription) {
            this.issueDescription = issueDescription;
        }
}
