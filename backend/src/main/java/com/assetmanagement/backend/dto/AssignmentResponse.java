package com.assetmanagement.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class AssignmentResponse {

    private Long assignmentFormId;

        private String formCode;

        private LocalDate assignmentDate;

        private String status;

        private String reason;

        private String note;

        private ReferenceResponse sourceDepartment;

        private ReferenceResponse targetDepartment;

        private UserReferenceResponse targetUser;

        private UserReferenceResponse issuedByUser;

        private UserReferenceResponse approvedByUser;

        private List<AssignmentDetailResponse> details;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

    public AssignmentResponse() {
        }

    public AssignmentResponse(Long assignmentFormId, String formCode, LocalDate assignmentDate, String status, String reason, String note, ReferenceResponse sourceDepartment, ReferenceResponse targetDepartment, UserReferenceResponse targetUser, UserReferenceResponse issuedByUser, UserReferenceResponse approvedByUser, List<AssignmentDetailResponse> details, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.assignmentFormId = assignmentFormId;
            this.formCode = formCode;
            this.assignmentDate = assignmentDate;
            this.status = status;
            this.reason = reason;
            this.note = note;
            this.sourceDepartment = sourceDepartment;
            this.targetDepartment = targetDepartment;
            this.targetUser = targetUser;
            this.issuedByUser = issuedByUser;
            this.approvedByUser = approvedByUser;
            this.details = details;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

    public Long getAssignmentFormId() {
            return assignmentFormId;
        }

    public void setAssignmentFormId(Long assignmentFormId) {
            this.assignmentFormId = assignmentFormId;
        }

    public String getFormCode() {
            return formCode;
        }

    public void setFormCode(String formCode) {
            this.formCode = formCode;
        }

    public LocalDate getAssignmentDate() {
            return assignmentDate;
        }

    public void setAssignmentDate(LocalDate assignmentDate) {
            this.assignmentDate = assignmentDate;
        }

    public String getStatus() {
            return status;
        }

    public void setStatus(String status) {
            this.status = status;
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

    public ReferenceResponse getSourceDepartment() {
            return sourceDepartment;
        }

    public void setSourceDepartment(ReferenceResponse sourceDepartment) {
            this.sourceDepartment = sourceDepartment;
        }

    public ReferenceResponse getTargetDepartment() {
            return targetDepartment;
        }

    public void setTargetDepartment(ReferenceResponse targetDepartment) {
            this.targetDepartment = targetDepartment;
        }

    public UserReferenceResponse getTargetUser() {
            return targetUser;
        }

    public void setTargetUser(UserReferenceResponse targetUser) {
            this.targetUser = targetUser;
        }

    public UserReferenceResponse getIssuedByUser() {
            return issuedByUser;
        }

    public void setIssuedByUser(UserReferenceResponse issuedByUser) {
            this.issuedByUser = issuedByUser;
        }

    public UserReferenceResponse getApprovedByUser() {
            return approvedByUser;
        }

    public void setApprovedByUser(UserReferenceResponse approvedByUser) {
            this.approvedByUser = approvedByUser;
        }

    public List<AssignmentDetailResponse> getDetails() {
            return details;
        }

    public void setDetails(List<AssignmentDetailResponse> details) {
            this.details = details;
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

    public static AssignmentResponseBuilder builder() {
            return new AssignmentResponseBuilder();
        }

    public static class AssignmentResponseBuilder {
            private Long assignmentFormId;
            private String formCode;
            private LocalDate assignmentDate;
            private String status;
            private String reason;
            private String note;
            private ReferenceResponse sourceDepartment;
            private ReferenceResponse targetDepartment;
            private UserReferenceResponse targetUser;
            private UserReferenceResponse issuedByUser;
            private UserReferenceResponse approvedByUser;
            private List<AssignmentDetailResponse> details;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public AssignmentResponseBuilder assignmentFormId(Long assignmentFormId) {
                this.assignmentFormId = assignmentFormId;
                return this;
            }

            public AssignmentResponseBuilder formCode(String formCode) {
                this.formCode = formCode;
                return this;
            }

            public AssignmentResponseBuilder assignmentDate(LocalDate assignmentDate) {
                this.assignmentDate = assignmentDate;
                return this;
            }

            public AssignmentResponseBuilder status(String status) {
                this.status = status;
                return this;
            }

            public AssignmentResponseBuilder reason(String reason) {
                this.reason = reason;
                return this;
            }

            public AssignmentResponseBuilder note(String note) {
                this.note = note;
                return this;
            }

            public AssignmentResponseBuilder sourceDepartment(ReferenceResponse sourceDepartment) {
                this.sourceDepartment = sourceDepartment;
                return this;
            }

            public AssignmentResponseBuilder targetDepartment(ReferenceResponse targetDepartment) {
                this.targetDepartment = targetDepartment;
                return this;
            }

            public AssignmentResponseBuilder targetUser(UserReferenceResponse targetUser) {
                this.targetUser = targetUser;
                return this;
            }

            public AssignmentResponseBuilder issuedByUser(UserReferenceResponse issuedByUser) {
                this.issuedByUser = issuedByUser;
                return this;
            }

            public AssignmentResponseBuilder approvedByUser(UserReferenceResponse approvedByUser) {
                this.approvedByUser = approvedByUser;
                return this;
            }

            public AssignmentResponseBuilder details(List<AssignmentDetailResponse> details) {
                this.details = details;
                return this;
            }

            public AssignmentResponseBuilder createdAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public AssignmentResponseBuilder updatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
                return this;
            }

            public AssignmentResponse build() {
                return new AssignmentResponse(assignmentFormId, formCode, assignmentDate, status, reason, note, sourceDepartment, targetDepartment, targetUser, issuedByUser, approvedByUser, details, createdAt, updatedAt);
            }
        }
}
