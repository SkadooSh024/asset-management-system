package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotNull;

public class IncidentAssignmentRequest {

    @NotNull(message = "Người thao tác không được để trống.")
        private Long actingUserId;

        @NotNull(message = "Người được phân công không được để trống.")
        private Long assignedToUserId;

        private String note;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }

    public Long getAssignedToUserId() {
            return assignedToUserId;
        }

    public void setAssignedToUserId(Long assignedToUserId) {
            this.assignedToUserId = assignedToUserId;
        }

    public String getNote() {
            return note;
        }

    public void setNote(String note) {
            this.note = note;
        }
}
