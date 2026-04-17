package com.assetmanagement.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class AssignmentItemRequest {

    @NotNull(message = "Tài sản trong phiếu cấp phát không được để trống.")
        private Long assetId;

        private LocalDate expectedReturnDate;

        private String note;

    public Long getAssetId() {
            return assetId;
        }

    public void setAssetId(Long assetId) {
            this.assetId = assetId;
        }

    public LocalDate getExpectedReturnDate() {
            return expectedReturnDate;
        }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
            this.expectedReturnDate = expectedReturnDate;
        }

    public String getNote() {
            return note;
        }

    public void setNote(String note) {
            this.note = note;
        }
}
