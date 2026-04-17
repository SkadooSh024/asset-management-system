package com.assetmanagement.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssetRequest {

    @NotBlank(message = "Mã tài sản không được để trống.")
        private String assetCode;

        @NotBlank(message = "Tên tài sản không được để trống.")
        private String assetName;

        @NotNull(message = "Danh mục tài sản không được để trống.")
        private Long categoryId;

        private Long currentStatusId;

        private Long owningDepartmentId;

        private Long currentDepartmentId;

        private Long assignedUserId;

        private String brand;

        private String model;

        private String serialNumber;

        private String assetTag;

        private LocalDate purchaseDate;

        private LocalDate warrantyExpiryDate;

        private BigDecimal purchaseCost;

        private String specificationText;

        private String notes;

        private String imageUrl;

        private Boolean isActive;

        @NotNull(message = "Người thao tác không được để trống.")
        private Long actingUserId;

    public String getAssetCode() {
            return assetCode;
        }

    public void setAssetCode(String assetCode) {
            this.assetCode = assetCode;
        }

    public String getAssetName() {
            return assetName;
        }

    public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

    public Long getCategoryId() {
            return categoryId;
        }

    public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

    public Long getCurrentStatusId() {
            return currentStatusId;
        }

    public void setCurrentStatusId(Long currentStatusId) {
            this.currentStatusId = currentStatusId;
        }

    public Long getOwningDepartmentId() {
            return owningDepartmentId;
        }

    public void setOwningDepartmentId(Long owningDepartmentId) {
            this.owningDepartmentId = owningDepartmentId;
        }

    public Long getCurrentDepartmentId() {
            return currentDepartmentId;
        }

    public void setCurrentDepartmentId(Long currentDepartmentId) {
            this.currentDepartmentId = currentDepartmentId;
        }

    public Long getAssignedUserId() {
            return assignedUserId;
        }

    public void setAssignedUserId(Long assignedUserId) {
            this.assignedUserId = assignedUserId;
        }

    public String getBrand() {
            return brand;
        }

    public void setBrand(String brand) {
            this.brand = brand;
        }

    public String getModel() {
            return model;
        }

    public void setModel(String model) {
            this.model = model;
        }

    public String getSerialNumber() {
            return serialNumber;
        }

    public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

    public String getAssetTag() {
            return assetTag;
        }

    public void setAssetTag(String assetTag) {
            this.assetTag = assetTag;
        }

    public LocalDate getPurchaseDate() {
            return purchaseDate;
        }

    public void setPurchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

    public LocalDate getWarrantyExpiryDate() {
            return warrantyExpiryDate;
        }

    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
            this.warrantyExpiryDate = warrantyExpiryDate;
        }

    public BigDecimal getPurchaseCost() {
            return purchaseCost;
        }

    public void setPurchaseCost(BigDecimal purchaseCost) {
            this.purchaseCost = purchaseCost;
        }

    public String getSpecificationText() {
            return specificationText;
        }

    public void setSpecificationText(String specificationText) {
            this.specificationText = specificationText;
        }

    public String getNotes() {
            return notes;
        }

    public void setNotes(String notes) {
            this.notes = notes;
        }

    public String getImageUrl() {
            return imageUrl;
        }

    public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    public Boolean getIsActive() {
            return isActive;
        }

    public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
        }
}
