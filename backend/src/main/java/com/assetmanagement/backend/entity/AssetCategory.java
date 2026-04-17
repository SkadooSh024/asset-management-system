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
@Table(name = "asset_categories")
public class AssetCategory {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "category_id")
        private Long categoryId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_category_id")
        private AssetCategory parentCategory;

        @Column(name = "category_code", nullable = false)
        private String categoryCode;

        @Column(name = "category_name", nullable = false)
        private String categoryName;

        @Column(name = "default_warranty_months")
        private Integer defaultWarrantyMonths;

        @Column(name = "default_maintenance_cycle_days")
        private Integer defaultMaintenanceCycleDays;

        @Column(name = "description")
        private String description;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive;

        @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updatedAt;

    public AssetCategory() {
        }

    public Long getCategoryId() {
            return categoryId;
        }

    public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

    public AssetCategory getParentCategory() {
            return parentCategory;
        }

    public void setParentCategory(AssetCategory parentCategory) {
            this.parentCategory = parentCategory;
        }

    public String getCategoryCode() {
            return categoryCode;
        }

    public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

    public String getCategoryName() {
            return categoryName;
        }

    public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

    public Integer getDefaultWarrantyMonths() {
            return defaultWarrantyMonths;
        }

    public void setDefaultWarrantyMonths(Integer defaultWarrantyMonths) {
            this.defaultWarrantyMonths = defaultWarrantyMonths;
        }

    public Integer getDefaultMaintenanceCycleDays() {
            return defaultMaintenanceCycleDays;
        }

    public void setDefaultMaintenanceCycleDays(Integer defaultMaintenanceCycleDays) {
            this.defaultMaintenanceCycleDays = defaultMaintenanceCycleDays;
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
