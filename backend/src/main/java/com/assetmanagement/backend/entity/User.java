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
@Table(name = "users")
public class User {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long userId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "role_id", nullable = false)
        private Role role;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "department_id")
        private Department department;

        @Column(name = "username", nullable = false)
        private String username;

        @Column(name = "full_name", nullable = false)
        private String fullName;

        @Column(name = "email", nullable = false)
        private String email;

        @Column(name = "phone")
        private String phone;

        @Column(name = "job_title")
        private String jobTitle;

        @Column(name = "password_hash", nullable = false)
        private String passwordHash;

        @Column(name = "status", nullable = false)
        private String status;

        @Column(name = "last_login_at")
        private LocalDateTime lastLoginAt;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

    public User() {
        }

    public Long getUserId() {
            return userId;
        }

    public void setUserId(Long userId) {
            this.userId = userId;
        }

    public Role getRole() {
            return role;
        }

    public void setRole(Role role) {
            this.role = role;
        }

    public Department getDepartment() {
            return department;
        }

    public void setDepartment(Department department) {
            this.department = department;
        }

    public String getUsername() {
            return username;
        }

    public void setUsername(String username) {
            this.username = username;
        }

    public String getFullName() {
            return fullName;
        }

    public void setFullName(String fullName) {
            this.fullName = fullName;
        }

    public String getEmail() {
            return email;
        }

    public void setEmail(String email) {
            this.email = email;
        }

    public String getPhone() {
            return phone;
        }

    public void setPhone(String phone) {
            this.phone = phone;
        }

    public String getJobTitle() {
            return jobTitle;
        }

    public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

    public String getPasswordHash() {
            return passwordHash;
        }

    public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }

    public String getStatus() {
            return status;
        }

    public void setStatus(String status) {
            this.status = status;
        }

    public LocalDateTime getLastLoginAt() {
            return lastLoginAt;
        }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
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
