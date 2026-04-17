package com.assetmanagement.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class MaintenanceTicketResponse {

    private Long maintenanceTicketId;

        private String ticketCode;

        private Long incidentReportId;

        private ReferenceResponse asset;

        private UserReferenceResponse assignedToUser;

        private LocalDateTime openedDate;

        private String status;

        private String priority;

        private String maintenanceType;

        private String problemDescription;

        private String externalServiceName;

        private BigDecimal estimatedCost;

        private BigDecimal actualCost;

        private LocalDateTime completedDate;

        private String resultSummary;

        private List<MaintenanceUpdateResponse> updates;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

    public MaintenanceTicketResponse() {
        }

    public MaintenanceTicketResponse(Long maintenanceTicketId, String ticketCode, Long incidentReportId, ReferenceResponse asset, UserReferenceResponse assignedToUser, LocalDateTime openedDate, String status, String priority, String maintenanceType, String problemDescription, String externalServiceName, BigDecimal estimatedCost, BigDecimal actualCost, LocalDateTime completedDate, String resultSummary, List<MaintenanceUpdateResponse> updates, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.maintenanceTicketId = maintenanceTicketId;
            this.ticketCode = ticketCode;
            this.incidentReportId = incidentReportId;
            this.asset = asset;
            this.assignedToUser = assignedToUser;
            this.openedDate = openedDate;
            this.status = status;
            this.priority = priority;
            this.maintenanceType = maintenanceType;
            this.problemDescription = problemDescription;
            this.externalServiceName = externalServiceName;
            this.estimatedCost = estimatedCost;
            this.actualCost = actualCost;
            this.completedDate = completedDate;
            this.resultSummary = resultSummary;
            this.updates = updates;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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

    public Long getIncidentReportId() {
            return incidentReportId;
        }

    public void setIncidentReportId(Long incidentReportId) {
            this.incidentReportId = incidentReportId;
        }

    public ReferenceResponse getAsset() {
            return asset;
        }

    public void setAsset(ReferenceResponse asset) {
            this.asset = asset;
        }

    public UserReferenceResponse getAssignedToUser() {
            return assignedToUser;
        }

    public void setAssignedToUser(UserReferenceResponse assignedToUser) {
            this.assignedToUser = assignedToUser;
        }

    public LocalDateTime getOpenedDate() {
            return openedDate;
        }

    public void setOpenedDate(LocalDateTime openedDate) {
            this.openedDate = openedDate;
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

    public List<MaintenanceUpdateResponse> getUpdates() {
            return updates;
        }

    public void setUpdates(List<MaintenanceUpdateResponse> updates) {
            this.updates = updates;
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

    public static MaintenanceTicketResponseBuilder builder() {
            return new MaintenanceTicketResponseBuilder();
        }

    public static class MaintenanceTicketResponseBuilder {
            private Long maintenanceTicketId;
            private String ticketCode;
            private Long incidentReportId;
            private ReferenceResponse asset;
            private UserReferenceResponse assignedToUser;
            private LocalDateTime openedDate;
            private String status;
            private String priority;
            private String maintenanceType;
            private String problemDescription;
            private String externalServiceName;
            private BigDecimal estimatedCost;
            private BigDecimal actualCost;
            private LocalDateTime completedDate;
            private String resultSummary;
            private List<MaintenanceUpdateResponse> updates;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public MaintenanceTicketResponseBuilder maintenanceTicketId(Long maintenanceTicketId) {
                this.maintenanceTicketId = maintenanceTicketId;
                return this;
            }

            public MaintenanceTicketResponseBuilder ticketCode(String ticketCode) {
                this.ticketCode = ticketCode;
                return this;
            }

            public MaintenanceTicketResponseBuilder incidentReportId(Long incidentReportId) {
                this.incidentReportId = incidentReportId;
                return this;
            }

            public MaintenanceTicketResponseBuilder asset(ReferenceResponse asset) {
                this.asset = asset;
                return this;
            }

            public MaintenanceTicketResponseBuilder assignedToUser(UserReferenceResponse assignedToUser) {
                this.assignedToUser = assignedToUser;
                return this;
            }

            public MaintenanceTicketResponseBuilder openedDate(LocalDateTime openedDate) {
                this.openedDate = openedDate;
                return this;
            }

            public MaintenanceTicketResponseBuilder status(String status) {
                this.status = status;
                return this;
            }

            public MaintenanceTicketResponseBuilder priority(String priority) {
                this.priority = priority;
                return this;
            }

            public MaintenanceTicketResponseBuilder maintenanceType(String maintenanceType) {
                this.maintenanceType = maintenanceType;
                return this;
            }

            public MaintenanceTicketResponseBuilder problemDescription(String problemDescription) {
                this.problemDescription = problemDescription;
                return this;
            }

            public MaintenanceTicketResponseBuilder externalServiceName(String externalServiceName) {
                this.externalServiceName = externalServiceName;
                return this;
            }

            public MaintenanceTicketResponseBuilder estimatedCost(BigDecimal estimatedCost) {
                this.estimatedCost = estimatedCost;
                return this;
            }

            public MaintenanceTicketResponseBuilder actualCost(BigDecimal actualCost) {
                this.actualCost = actualCost;
                return this;
            }

            public MaintenanceTicketResponseBuilder completedDate(LocalDateTime completedDate) {
                this.completedDate = completedDate;
                return this;
            }

            public MaintenanceTicketResponseBuilder resultSummary(String resultSummary) {
                this.resultSummary = resultSummary;
                return this;
            }

            public MaintenanceTicketResponseBuilder updates(List<MaintenanceUpdateResponse> updates) {
                this.updates = updates;
                return this;
            }

            public MaintenanceTicketResponseBuilder createdAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public MaintenanceTicketResponseBuilder updatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
                return this;
            }

            public MaintenanceTicketResponse build() {
                return new MaintenanceTicketResponse(maintenanceTicketId, ticketCode, incidentReportId, asset, assignedToUser, openedDate, status, priority, maintenanceType, problemDescription, externalServiceName, estimatedCost, actualCost, completedDate, resultSummary, updates, createdAt, updatedAt);
            }
        }
}
