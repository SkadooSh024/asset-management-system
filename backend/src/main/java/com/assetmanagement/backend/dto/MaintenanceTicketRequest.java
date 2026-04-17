package com.assetmanagement.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaintenanceTicketRequest {

    @NotNull(message = "Người thao tác không được để trống.")
        private Long actingUserId;

        private Long incidentReportId;

        @NotNull(message = "Tài sản không được để trống.")
        private Long assetId;

        private Long assignedToUserId;

        @NotBlank(message = "Mức độ ưu tiên không được để trống.")
        private String priority;

        @NotBlank(message = "Loại bảo trì không được để trống.")
        private String maintenanceType;

        @NotBlank(message = "Mô tả vấn đề không được để trống.")
        private String problemDescription;

        private String externalServiceName;

        private BigDecimal estimatedCost;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }

    public Long getIncidentReportId() {
            return incidentReportId;
        }

    public void setIncidentReportId(Long incidentReportId) {
            this.incidentReportId = incidentReportId;
        }

    public Long getAssetId() {
            return assetId;
        }

    public void setAssetId(Long assetId) {
            this.assetId = assetId;
        }

    public Long getAssignedToUserId() {
            return assignedToUserId;
        }

    public void setAssignedToUserId(Long assignedToUserId) {
            this.assignedToUserId = assignedToUserId;
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
}
