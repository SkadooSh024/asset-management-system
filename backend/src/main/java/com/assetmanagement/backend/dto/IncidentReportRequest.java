package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class IncidentReportRequest {

    @NotNull(message = "Tai san khong duoc de trong.")
        private Long assetId;

        @NotNull(message = "Nguoi bao hong khong duoc de trong.")
        private Long reportedByUserId;

        private Long assignedToUserId;

        @NotBlank(message = "Muc do uu tien khong duoc de trong.")
        private String severity;

        @NotBlank(message = "Tieu de su co khong duoc de trong.")
        private String issueTitle;

        @NotBlank(message = "Mo ta su co khong duoc de trong.")
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
