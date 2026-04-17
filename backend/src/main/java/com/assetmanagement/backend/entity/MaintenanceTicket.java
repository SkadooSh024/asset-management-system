package com.assetmanagement.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "maintenance_tickets")
public class MaintenanceTicket {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "maintenance_ticket_id")
        private Long maintenanceTicketId;

        @Column(name = "ticket_code", nullable = false)
        private String ticketCode;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "incident_report_id")
        private IncidentReport incidentReport;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @Column(name = "opened_date", nullable = false, insertable = false, updatable = false)
        private LocalDateTime openedDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_to_user_id")
        private User assignedToUser;

        @Column(name = "status", nullable = false)
        private String status;

        @Column(name = "priority", nullable = false)
        private String priority;

        @Column(name = "maintenance_type", nullable = false)
        private String maintenanceType;

        @Column(name = "problem_description", nullable = false)
        private String problemDescription;

        @Column(name = "external_service_name")
        private String externalServiceName;

        @Column(name = "estimated_cost")
        private BigDecimal estimatedCost;

        @Column(name = "actual_cost")
        private BigDecimal actualCost;

        @Column(name = "completed_date")
        private LocalDateTime completedDate;

        @Column(name = "result_summary")
        private String resultSummary;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "created_by")
        private User createdBy;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "updated_by")
        private User updatedBy;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

        @OneToMany(mappedBy = "maintenanceTicket", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<MaintenanceUpdate> updates = new ArrayList<>();

    public MaintenanceTicket() {
        }

    public Long getMaintenanceTicketId() {
            return maintenanceTicketId;
        }

    public void setMaintenanceTicketId(Long maintenanceTicketId) {
            this.maintenanceTicketId = maintenanceTicketId;
        }

    public String getTicketCode() {
            return ticketCode;
        }

    public void setTicketCode(String ticketCode) {
            this.ticketCode = ticketCode;
        }

    public IncidentReport getIncidentReport() {
            return incidentReport;
        }

    public void setIncidentReport(IncidentReport incidentReport) {
            this.incidentReport = incidentReport;
        }

    public Asset getAsset() {
            return asset;
        }

    public void setAsset(Asset asset) {
            this.asset = asset;
        }

    public LocalDateTime getOpenedDate() {
            return openedDate;
        }

    public void setOpenedDate(LocalDateTime openedDate) {
            this.openedDate = openedDate;
        }

    public User getAssignedToUser() {
            return assignedToUser;
        }

    public void setAssignedToUser(User assignedToUser) {
            this.assignedToUser = assignedToUser;
        }

    public String getStatus() {
            return status;
        }

    public void setStatus(String status) {
            this.status = status;
        }

    public String getPriority() {
            return priority;
        }

    public void setPriority(String priority) {
            this.priority = priority;
        }

    public String getMaintenanceType() {
            return maintenanceType;
        }

    public void setMaintenanceType(String maintenanceType) {
            this.maintenanceType = maintenanceType;
        }

    public String getProblemDescription() {
            return problemDescription;
        }

    public void setProblemDescription(String problemDescription) {
            this.problemDescription = problemDescription;
        }

    public String getExternalServiceName() {
            return externalServiceName;
        }

    public void setExternalServiceName(String externalServiceName) {
            this.externalServiceName = externalServiceName;
        }

    public BigDecimal getEstimatedCost() {
            return estimatedCost;
        }

    public void setEstimatedCost(BigDecimal estimatedCost) {
            this.estimatedCost = estimatedCost;
        }

    public BigDecimal getActualCost() {
            return actualCost;
        }

    public void setActualCost(BigDecimal actualCost) {
            this.actualCost = actualCost;
        }

    public LocalDateTime getCompletedDate() {
            return completedDate;
        }

    public void setCompletedDate(LocalDateTime completedDate) {
            this.completedDate = completedDate;
        }

    public String getResultSummary() {
            return resultSummary;
        }

    public void setResultSummary(String resultSummary) {
            this.resultSummary = resultSummary;
        }

    public User getCreatedBy() {
            return createdBy;
        }

    public void setCreatedBy(User createdBy) {
            this.createdBy = createdBy;
        }

    public User getUpdatedBy() {
            return updatedBy;
        }

    public void setUpdatedBy(User updatedBy) {
            this.updatedBy = updatedBy;
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

    public List<MaintenanceUpdate> getUpdates() {
            return updates;
        }

    public void setUpdates(List<MaintenanceUpdate> updates) {
            this.updates = updates;
        }
}
