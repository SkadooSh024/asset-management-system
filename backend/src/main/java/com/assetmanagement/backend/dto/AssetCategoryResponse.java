package com.assetmanagement.backend.dto;

import java.time.LocalDateTime;


public class AssetCategoryResponse {

    private Long categoryId;

        private String categoryCode;

        private String categoryName;

        private Integer defaultWarrantyMonths;

        private Integer defaultMaintenanceCycleDays;

        private String description;

        private Boolean isActive;

        private ReferenceResponse parentCategory;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

    public AssetCategoryResponse() {
        }

    public AssetCategoryResponse(Long categoryId, String categoryCode, String categoryName, Integer defaultWarrantyMonths, Integer defaultMaintenanceCycleDays, String description, Boolean isActive, ReferenceResponse parentCategory, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.categoryId = categoryId;
            this.categoryCode = categoryCode;
            this.categoryName = categoryName;
            this.defaultWarrantyMonths = defaultWarrantyMonths;
            this.defaultMaintenanceCycleDays = defaultMaintenanceCycleDays;
            this.description = description;
            this.isActive = isActive;
            this.parentCategory = parentCategory;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

    public Long getCategoryId() {
            return categoryId;
        }

    public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
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

    public ReferenceResponse getParentCategory() {
            return parentCategory;
        }

    public void setParentCategory(ReferenceResponse parentCategory) {
            this.parentCategory = parentCategory;
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

    public static AssetCategoryResponseBuilder builder() {
            return new AssetCategoryResponseBuilder();
        }

    public static class AssetCategoryResponseBuilder {
            private Long categoryId;
            private String categoryCode;
            private String categoryName;
            private Integer defaultWarrantyMonths;
            private Integer defaultMaintenanceCycleDays;
            private String description;
            private Boolean isActive;
            private ReferenceResponse parentCategory;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public AssetCategoryResponseBuilder categoryId(Long categoryId) {
                this.categoryId = categoryId;
                return this;
            }

            public AssetCategoryResponseBuilder categoryCode(String categoryCode) {
                this.categoryCode = categoryCode;
                return this;
            }

            public AssetCategoryResponseBuilder categoryName(String categoryName) {
                this.categoryName = categoryName;
                return this;
            }

            public AssetCategoryResponseBuilder defaultWarrantyMonths(Integer defaultWarrantyMonths) {
                this.defaultWarrantyMonths = defaultWarrantyMonths;
                return this;
            }

            public AssetCategoryResponseBuilder defaultMaintenanceCycleDays(Integer defaultMaintenanceCycleDays) {
                this.defaultMaintenanceCycleDays = defaultMaintenanceCycleDays;
                return this;
            }

            public AssetCategoryResponseBuilder description(String description) {
                this.description = description;
                return this;
            }

            public AssetCategoryResponseBuilder isActive(Boolean isActive) {
                this.isActive = isActive;
                return this;
            }

            public AssetCategoryResponseBuilder parentCategory(ReferenceResponse parentCategory) {
                this.parentCategory = parentCategory;
                return this;
            }

            public AssetCategoryResponseBuilder createdAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public AssetCategoryResponseBuilder updatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
                return this;
            }

            public AssetCategoryResponse build() {
                return new AssetCategoryResponse(categoryId, categoryCode, categoryName, defaultWarrantyMonths, defaultMaintenanceCycleDays, description, isActive, parentCategory, createdAt, updatedAt);
            }
        }
}
