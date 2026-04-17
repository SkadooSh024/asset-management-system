package com.assetmanagement.backend.entity;

import java.time.LocalDateTime;

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
@Table(name = "departments")
public class Department {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "department_id")
        private Long departmentId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_department_id")
        private Department parentDepartment;

        @Column(name = "department_code", nullable = false)
        private String departmentCode;

        @Column(name = "department_name", nullable = false)
        private String departmentName;

        @Column(name = "manager_name")
        private String managerName;

        @Column(name = "phone")
        private String phone;

        @Column(name = "email")
        private String email;

        @Column(name = "description")
        private String description;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

    public Department() {
        }

    public Long getDepartmentId() {
            return departmentId;
        }

    public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
        }

    public Department getParentDepartment() {
            return parentDepartment;
        }

    public void setParentDepartment(Department parentDepartment) {
            this.parentDepartment = parentDepartment;
        }

    public String getDepartmentCode() {
            return departmentCode;
        }

    public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

    public String getDepartmentName() {
            return departmentName;
        }

    public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

    public String getManagerName() {
            return managerName;
        }

    public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

    public String getPhone() {
            return phone;
        }

    public void setPhone(String phone) {
            this.phone = phone;
        }

    public String getEmail() {
            return email;
        }

    public void setEmail(String email) {
            this.email = email;
        }

    public String getDescription() {
            return description;
        }

    public void setDescription(String description) {
            this.description = description;
        }

    public Boolean getIsActive() {
            return isActive;
        }

    public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
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
}
