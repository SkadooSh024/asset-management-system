package com.assetmanagement.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaintenanceUpdateRequest {

    @NotNull(message = "Nguoi thao tac khong duoc de trong.")
        private Long actingUserId;

        private String updateStatus;

        @NotBlank(message = "Noi dung cap nhat khong duoc de trong.")
        private String updateNote;

        private LocalDate nextActionDate;

        private BigDecimal actualCost;

        private String externalServiceName;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }

    public String getUpdateStatus() {
            return updateStatus;
        }

    public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

    public String getUpdateNote() {
            return updateNote;
        }

    public void setUpdateNote(String updateNote) {
            this.updateNote = updateNote;
        }

    public LocalDate getNextActionDate() {
            return nextActionDate;
        }

    public void setNextActionDate(LocalDate nextActionDate) {
            this.nextActionDate = nextActionDate;
        }

    public BigDecimal getActualCost() {
            return actualCost;
        }

    public void setActualCost(BigDecimal actualCost) {
            this.actualCost = actualCost;
        }

    public String getExternalServiceName() {
            return externalServiceName;
        }

    public void setExternalServiceName(String externalServiceName) {
            this.externalServiceName = externalServiceName;
        }
}
