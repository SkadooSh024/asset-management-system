package com.assetmanagement.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "incident_reports")
public class IncidentReport {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "incident_report_id")
        private Long incidentReportId;

        @Column(name = "report_code", nullable = false)
        private String reportCode;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reported_by_user_id", nullable = false)
        private User reportedByUser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_to_user_id")
        private User assignedToUser;

        @Column(name = "report_date", nullable = false, insertable = false, updatable = false)
        private LocalDateTime reportDate;

        @Column(name = "severity", nullable = false)
        private String severity;

        @Column(name = "issue_title", nullable = false)
        private String issueTitle;

        @Column(name = "issue_description", nullable = false)
        private String issueDescription;

        @Column(name = "status", nullable = false)
        private String status;

        @Column(name = "resolution_note")
        private String resolutionNote;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

    public IncidentReport() {
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

    public Asset getAsset() {
            return asset;
        }

    public void setAsset(Asset asset) {
            this.asset = asset;
        }

    public User getReportedByUser() {
            return reportedByUser;
        }

    public void setReportedByUser(User reportedByUser) {
            this.reportedByUser = reportedByUser;
        }

    public User getAssignedToUser() {
            return assignedToUser;
        }

    public void setAssignedToUser(User assignedToUser) {
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
}
