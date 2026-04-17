package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotNull;

public class AssignmentActionRequest {

    @NotNull(message = "Nguoi thao tac khong duoc de trong.")
        private Long actingUserId;

        private String note;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }

    public String getNote() {
            return note;
        }

    public void setNote(String note) {
            this.note = note;
        }
}
