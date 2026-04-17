package com.assetmanagement.backend.dto;

import java.time.LocalDateTime;


public class IncidentResponse {

    private Long incidentReportId;

        private String reportCode;

        private ReferenceResponse asset;

        private UserReferenceResponse reportedByUser;

        private UserReferenceResponse assignedToUser;

        private LocalDateTime reportDate;

        private String severity;

        private String issueTitle;

        private String issueDescription;

        private String status;

        private String resolutionNote;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

    public IncidentResponse() {
        }

    public IncidentResponse(Long incidentReportId, String reportCode, ReferenceResponse asset, UserReferenceResponse reportedByUser, UserReferenceResponse assignedToUser, LocalDateTime reportDate, String severity, String issueTitle, String issueDescription, String status, String resolutionNote, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.incidentReportId = incidentReportId;
            this.reportCode = reportCode;
            this.asset = asset;
            this.reportedByUser = reportedByUser;
            this.assignedToUser = assignedToUser;
            this.reportDate = reportDate;
            this.severity = severity;
            this.issueTitle = issueTitle;
            this.issueDescription = issueDescription;
            this.status = status;
            this.resolutionNote = resolutionNote;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

    public Long getIncidentReportId() {
            return incidentReportId;
        }

    public void setIncidentReportId(Long incidentReportId) {
            this.incidentReportId = incidentReportId;
        }

    public String getReportCode() {
            return reportCode;
        }

    public void setReportCode(String reportCode) {
            this.reportCode = reportCode;
        }

    public ReferenceResponse getAsset() {
            return asset;
        }

    public void setAsset(ReferenceResponse asset) {
            this.asset = asset;
        }

    public UserReferenceResponse getReportedByUser() {
            return reportedByUser;
        }

    public void setReportedByUser(UserReferenceResponse reportedByUser) {
            this.reportedByUser = reportedByUser;
        }

    public UserReferenceResponse getAssignedToUser() {
            return assignedToUser;
        }

    public void setAssignedToUser(UserReferenceResponse assignedToUser) {
            this.assignedToUser = assignedToUser;
        }

    public LocalDateTime getReportDate() {
            return reportDate;
        }

    public void setReportDate(LocalDateTime reportDate) {
            this.reportDate = reportDate;
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

    public String getStatus() {
            return status;
        }

    public void setStatus(String status) {
            this.status = status;
        }

    public String getResolutionNote() {
            return resolutionNote;
        }

    public void setResolutionNote(String resolutionNote) {
            this.resolutionNote = resolutionNote;
        }

    public LocalDateTime getCreatedAt() {
            return createdAt;
        }

    public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

    public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

    public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

    public static IncidentResponseBuilder builder() {
            return new IncidentResponseBuilder();
        }

    public static class IncidentResponseBuilder {
            private Long incidentReportId;
            private String reportCode;
            private ReferenceResponse asset;
            private UserReferenceResponse reportedByUser;
            private UserReferenceResponse assignedToUser;
            private LocalDateTime reportDate;
            private String severity;
            private String issueTitle;
            private String issueDescription;
            private String status;
            private String resolutionNote;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public IncidentResponseBuilder incidentReportId(Long incidentReportId) {
                this.incidentReportId = incidentReportId;
                return this;
            }

            public IncidentResponseBuilder reportCode(String reportCode) {
                this.reportCode = reportCode;
                return this;
            }

            public IncidentResponseBuilder asset(ReferenceResponse asset) {
                this.asset = asset;
                return this;
            }

            public IncidentResponseBuilder reportedByUser(UserReferenceResponse reportedByUser) {
                this.reportedByUser = reportedByUser;
                return this;
            }

            public IncidentResponseBuilder assignedToUser(UserReferenceResponse assignedToUser) {
                this.assignedToUser = assignedToUser;
                return this;
            }

            public IncidentResponseBuilder reportDate(LocalDateTime reportDate) {
                this.reportDate = reportDate;
                return this;
            }

            public IncidentResponseBuilder severity(String severity) {
                this.severity = severity;
                return this;
            }

            public IncidentResponseBuilder issueTitle(String issueTitle) {
                this.issueTitle = issueTitle;
                return this;
            }

            public IncidentResponseBuilder issueDescription(String issueDescription) {
                this.issueDescription = issueDescription;
                return this;
            }

            public IncidentResponseBuilder status(String status) {
                this.status = status;
                return this;
            }

            public IncidentResponseBuilder resolutionNote(String resolutionNote) {
                this.resolutionNote = resolutionNote;
                return this;
            }

            public IncidentResponseBuilder createdAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public IncidentResponseBuilder updatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
                return this;
            }

            public IncidentResponse build() {
                return new IncidentResponse(incidentReportId, reportCode, asset, reportedByUser, assignedToUser, reportDate, severity, issueTitle, issueDescription, status, resolutionNote, createdAt, updatedAt);
            }
        }
}
