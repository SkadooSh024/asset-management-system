package com.assetmanagement.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "assignment_forms")
public class AssignmentForm {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "assignment_form_id")
        private Long assignmentFormId;

        @Column(name = "form_code", nullable = false)
        private String formCode;

        @Column(name = "assignment_date", nullable = false)
        private LocalDate assignmentDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "source_department_id")
        private Department sourceDepartment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "target_department_id")
        private Department targetDepartment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "target_user_id")
        private User targetUser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "issued_by_user_id", nullable = false)
        private User issuedByUser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "approved_by_user_id")
        private User approvedByUser;

        @Column(name = "status", nullable = false)
        private String status;

        @Column(name = "reason")
        private String reason;

        @Column(name = "note")
        private String note;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "created_by")
        private User createdBy;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "updated_by")
        private User updatedBy;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

        @OneToMany(mappedBy = "assignmentForm", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<AssignmentFormDetail> details = new ArrayList<>();

    public AssignmentForm() {
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

    public Department getSourceDepartment() {
            return sourceDepartment;
        }

    public void setSourceDepartment(Department sourceDepartment) {
            this.sourceDepartment = sourceDepartment;
        }

    public Department getTargetDepartment() {
            return targetDepartment;
        }

    public void setTargetDepartment(Department targetDepartment) {
            this.targetDepartment = targetDepartment;
        }

    public User getTargetUser() {
            return targetUser;
        }

    public void setTargetUser(User targetUser) {
            this.targetUser = targetUser;
        }

    public User getIssuedByUser() {
            return issuedByUser;
        }

    public void setIssuedByUser(User issuedByUser) {
            this.issuedByUser = issuedByUser;
        }

    public User getApprovedByUser() {
            return approvedByUser;
        }

    public void setApprovedByUser(User approvedByUser) {
            this.approvedByUser = approvedByUser;
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

    public User getCreatedBy() {
            return createdBy;
        }

    public void setCreatedBy(User createdBy) {
            this.createdBy = createdBy;
        }

    public User getUpdatedBy() {
            return updatedBy;
        }

    public void setUpdatedBy(User updatedBy) {
            this.updatedBy = updatedBy;
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

    public List<AssignmentFormDetail> getDetails() {
            return details;
        }

    public void setDetails(List<AssignmentFormDetail> details) {
            this.details = details;
        }
}
