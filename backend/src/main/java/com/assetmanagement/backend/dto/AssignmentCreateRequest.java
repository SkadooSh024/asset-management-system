package com.assetmanagement.backend.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AssignmentCreateRequest {

    @NotNull(message = "Người thao tác không được để trống.")
        private Long actingUserId;

        @NotNull(message = "Ngày cấp phát không được để trống.")
        private LocalDate assignmentDate;

        private Long sourceDepartmentId;

        private Long targetDepartmentId;

        private Long targetUserId;

        private String reason;

        private String note;

        @Valid
        @NotEmpty(message = "Phiếu cấp phát phải có ít nhất 1 tài sản.")
        private List<AssignmentItemRequest> details;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }

    public LocalDate getAssignmentDate() {
            return assignmentDate;
        }

    public void setAssignmentDate(LocalDate assignmentDate) {
            this.assignmentDate = assignmentDate;
        }

    public Long getSourceDepartmentId() {
            return sourceDepartmentId;
        }

    public void setSourceDepartmentId(Long sourceDepartmentId) {
            this.sourceDepartmentId = sourceDepartmentId;
        }

    public Long getTargetDepartmentId() {
            return targetDepartmentId;
        }

    public void setTargetDepartmentId(Long targetDepartmentId) {
            this.targetDepartmentId = targetDepartmentId;
        }

    public Long getTargetUserId() {
            return targetUserId;
        }

    public void setTargetUserId(Long targetUserId) {
            this.targetUserId = targetUserId;
        }

    public String getReason() {
            return reason;
        }

    public void setReason(String reason) {
            this.reason = reason;
        }

    public String getNote() {
            return note;
        }

    public void setNote(String note) {
            this.note = note;
        }

    public List<AssignmentItemRequest> getDetails() {
            return details;
        }

    public void setDetails(List<AssignmentItemRequest> details) {
            this.details = details;
        }
}
