package com.assetmanagement.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaintenanceCompleteRequest {

    @NotNull(message = "Người thao tác không được để trống.")
        private Long actingUserId;

        @NotBlank(message = "Kết quả bảo trì không được để trống.")
        private String resultSummary;

        private BigDecimal actualCost;

        private Long targetStatusId;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }

    public String getResultSummary() {
            return resultSummary;
        }

    public void setResultSummary(String resultSummary) {
            this.resultSummary = resultSummary;
        }

    public BigDecimal getActualCost() {
            return actualCost;
        }

    public void setActualCost(BigDecimal actualCost) {
            this.actualCost = actualCost;
        }

    public Long getTargetStatusId() {
            return targetStatusId;
        }

    public void setTargetStatusId(Long targetStatusId) {
            this.targetStatusId = targetStatusId;
        }
}
