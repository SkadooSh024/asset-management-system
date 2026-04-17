package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AssetCategoryRequest {

    private Long parentCategoryId;

        @NotBlank(message = "Mã danh mục không được để trống.")
        private String categoryCode;

        @NotBlank(message = "Tên danh mục không được để trống.")
        private String categoryName;

        private Integer defaultWarrantyMonths;

        private Integer defaultMaintenanceCycleDays;

        private String description;

        private Boolean isActive;

    public Long getParentCategoryId() {
            return parentCategoryId;
        }

    public void setParentCategoryId(Long parentCategoryId) {
            this.parentCategoryId = parentCategoryId;
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
}
