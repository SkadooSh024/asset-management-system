package com.assetmanagement.backend.dto;

import java.time.LocalDate;


public class AssignmentDetailResponse {

    private Long assignmentFormDetailId;

        private ReferenceResponse asset;

        private LocalDate expectedReturnDate;

        private String note;

    public AssignmentDetailResponse() {
        }

    public AssignmentDetailResponse(Long assignmentFormDetailId, ReferenceResponse asset, LocalDate expectedReturnDate, String note) {
            this.assignmentFormDetailId = assignmentFormDetailId;
            this.asset = asset;
            this.expectedReturnDate = expectedReturnDate;
            this.note = note;
        }

    public Long getAssignmentFormDetailId() {
            return assignmentFormDetailId;
        }

    public void setAssignmentFormDetailId(Long assignmentFormDetailId) {
            this.assignmentFormDetailId = assignmentFormDetailId;
        }

    public ReferenceResponse getAsset() {
            return asset;
        }

    public void setAsset(ReferenceResponse asset) {
            this.asset = asset;
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

    public static AssignmentDetailResponseBuilder builder() {
            return new AssignmentDetailResponseBuilder();
        }

    public static class AssignmentDetailResponseBuilder {
            private Long assignmentFormDetailId;
            private ReferenceResponse asset;
            private LocalDate expectedReturnDate;
            private String note;

            public AssignmentDetailResponseBuilder assignmentFormDetailId(Long assignmentFormDetailId) {
                this.assignmentFormDetailId = assignmentFormDetailId;
                return this;
            }

            public AssignmentDetailResponseBuilder asset(ReferenceResponse asset) {
                this.asset = asset;
                return this;
            }

            public AssignmentDetailResponseBuilder expectedReturnDate(LocalDate expectedReturnDate) {
                this.expectedReturnDate = expectedReturnDate;
                return this;
            }

            public AssignmentDetailResponseBuilder note(String note) {
                this.note = note;
                return this;
            }

            public AssignmentDetailResponse build() {
                return new AssignmentDetailResponse(assignmentFormDetailId, asset, expectedReturnDate, note);
            }
        }
}
