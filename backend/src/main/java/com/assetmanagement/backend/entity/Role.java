package com.assetmanagement.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "role_id")
        private Long roleId;

        @Column(name = "role_code", nullable = false)
        private String roleCode;

        @Column(name = "role_name", nullable = false)
        private String roleName;

        @Column(name = "description")
        private String description;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

    public Role() {
        }

    public Long getRoleId() {
            return roleId;
        }

    public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

    public String getRoleCode() {
            return roleCode;
        }

    public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

    public String getRoleName() {
            return roleName;
        }

    public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

    public String getDescription() {
            return description;
        }

    public void setDescription(String description) {
            this.description = description;
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
