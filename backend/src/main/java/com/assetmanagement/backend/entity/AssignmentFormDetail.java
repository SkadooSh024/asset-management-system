package com.assetmanagement.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "assignment_form_details")
public class AssignmentFormDetail {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "assignment_form_detail_id")
        private Long assignmentFormDetailId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assignment_form_id", nullable = false)
        private AssignmentForm assignmentForm;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @Column(name = "expected_return_date")
        private LocalDate expectedReturnDate;

        @Column(name = "note")
        private String note;

    public AssignmentFormDetail() {
        }

    public Long getAssignmentFormDetailId() {
            return assignmentFormDetailId;
        }

    public void setAssignmentFormDetailId(Long assignmentFormDetailId) {
            this.assignmentFormDetailId = assignmentFormDetailId;
        }

    public AssignmentForm getAssignmentForm() {
            return assignmentForm;
        }

    public void setAssignmentForm(AssignmentForm assignmentForm) {
            this.assignmentForm = assignmentForm;
        }

    public Asset getAsset() {
            return asset;
        }

    public void setAsset(Asset asset) {
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
}
