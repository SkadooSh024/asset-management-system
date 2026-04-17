package com.assetmanagement.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class AssetResponse {

    private Long assetId;

        private String assetCode;

        private String assetName;

        private ReferenceResponse category;

        private ReferenceResponse currentStatus;

        private ReferenceResponse owningDepartment;

        private ReferenceResponse currentDepartment;

        private UserReferenceResponse assignedUser;

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

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private List<AssetHistoryResponse> histories;

    public AssetResponse() {
        }

    public AssetResponse(Long assetId, String assetCode, String assetName, ReferenceResponse category, ReferenceResponse currentStatus, ReferenceResponse owningDepartment, ReferenceResponse currentDepartment, UserReferenceResponse assignedUser, String brand, String model, String serialNumber, String assetTag, LocalDate purchaseDate, LocalDate warrantyExpiryDate, BigDecimal purchaseCost, String specificationText, String notes, String imageUrl, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, List<AssetHistoryResponse> histories) {
            this.assetId = assetId;
            this.assetCode = assetCode;
            this.assetName = assetName;
            this.category = category;
            this.currentStatus = currentStatus;
            this.owningDepartment = owningDepartment;
            this.currentDepartment = currentDepartment;
            this.assignedUser = assignedUser;
            this.brand = brand;
            this.model = model;
            this.serialNumber = serialNumber;
            this.assetTag = assetTag;
            this.purchaseDate = purchaseDate;
            this.warrantyExpiryDate = warrantyExpiryDate;
            this.purchaseCost = purchaseCost;
            this.specificationText = specificationText;
            this.notes = notes;
            this.imageUrl = imageUrl;
            this.isActive = isActive;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.histories = histories;
        }

    public Long getAssetId() {
            return assetId;
        }

    public void setAssetId(Long assetId) {
            this.assetId = assetId;
        }

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

    public ReferenceResponse getCategory() {
            return category;
        }

    public void setCategory(ReferenceResponse category) {
            this.category = category;
        }

    public ReferenceResponse getCurrentStatus() {
            return currentStatus;
        }

    public void setCurrentStatus(ReferenceResponse currentStatus) {
            this.currentStatus = currentStatus;
        }

    public ReferenceResponse getOwningDepartment() {
            return owningDepartment;
        }

    public void setOwningDepartment(ReferenceResponse owningDepartment) {
            this.owningDepartment = owningDepartment;
        }

    public ReferenceResponse getCurrentDepartment() {
            return currentDepartment;
        }

    public void setCurrentDepartment(ReferenceResponse currentDepartment) {
            this.currentDepartment = currentDepartment;
        }

    public UserReferenceResponse getAssignedUser() {
            return assignedUser;
        }

    public void setAssignedUser(UserReferenceResponse assignedUser) {
            this.assignedUser = assignedUser;
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

    public List<AssetHistoryResponse> getHistories() {
            return histories;
        }

    public void setHistories(List<AssetHistoryResponse> histories) {
            this.histories = histories;
        }

    public static AssetResponseBuilder builder() {
            return new AssetResponseBuilder();
        }

    public static class AssetResponseBuilder {
            private Long assetId;
            private String assetCode;
            private String assetName;
            private ReferenceResponse category;
            private ReferenceResponse currentStatus;
            private ReferenceResponse owningDepartment;
            private ReferenceResponse currentDepartment;
            private UserReferenceResponse assignedUser;
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
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
            private List<AssetHistoryResponse> histories;

            public AssetResponseBuilder assetId(Long assetId) {
                this.assetId = assetId;
                return this;
            }

            public AssetResponseBuilder assetCode(String assetCode) {
                this.assetCode = assetCode;
                return this;
            }

            public AssetResponseBuilder assetName(String assetName) {
                this.assetName = assetName;
                return this;
            }

            public AssetResponseBuilder category(ReferenceResponse category) {
                this.category = category;
                return this;
            }

            public AssetResponseBuilder currentStatus(ReferenceResponse currentStatus) {
                this.currentStatus = currentStatus;
                return this;
            }

            public AssetResponseBuilder owningDepartment(ReferenceResponse owningDepartment) {
                this.owningDepartment = owningDepartment;
                return this;
            }

            public AssetResponseBuilder currentDepartment(ReferenceResponse currentDepartment) {
                this.currentDepartment = currentDepartment;
                return this;
            }

            public AssetResponseBuilder assignedUser(UserReferenceResponse assignedUser) {
                this.assignedUser = assignedUser;
                return this;
            }

            public AssetResponseBuilder brand(String brand) {
                this.brand = brand;
                return this;
            }

            public AssetResponseBuilder model(String model) {
                this.model = model;
                return this;
            }

            public AssetResponseBuilder serialNumber(String serialNumber) {
                this.serialNumber = serialNumber;
                return this;
            }

            public AssetResponseBuilder assetTag(String assetTag) {
                this.assetTag = assetTag;
                return this;
            }

            public AssetResponseBuilder purchaseDate(LocalDate purchaseDate) {
                this.purchaseDate = purchaseDate;
                return this;
            }

            public AssetResponseBuilder warrantyExpiryDate(LocalDate warrantyExpiryDate) {
                this.warrantyExpiryDate = warrantyExpiryDate;
                return this;
            }

            public AssetResponseBuilder purchaseCost(BigDecimal purchaseCost) {
                this.purchaseCost = purchaseCost;
                return this;
            }

            public AssetResponseBuilder specificationText(String specificationText) {
                this.specificationText = specificationText;
                return this;
            }

            public AssetResponseBuilder notes(String notes) {
                this.notes = notes;
                return this;
            }

            public AssetResponseBuilder imageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
                return this;
            }

            public AssetResponseBuilder isActive(Boolean isActive) {
                this.isActive = isActive;
                return this;
            }

            public AssetResponseBuilder createdAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public AssetResponseBuilder updatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
                return this;
            }

            public AssetResponseBuilder histories(List<AssetHistoryResponse> histories) {
                this.histories = histories;
                return this;
            }

            public AssetResponse build() {
                return new AssetResponse(assetId, assetCode, assetName, category, currentStatus, owningDepartment, currentDepartment, assignedUser, brand, model, serialNumber, assetTag, purchaseDate, warrantyExpiryDate, purchaseCost, specificationText, notes, imageUrl, isActive, createdAt, updatedAt, histories);
            }
        }
}
