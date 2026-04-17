package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class IncidentCloseRequest {

    @NotNull(message = "Nguoi thao tac khong duoc de trong.")
        private Long actingUserId;

        @NotBlank(message = "Trang thai dong su co khong duoc de trong.")
        private String status;

        private String resolutionNote;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
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
}
