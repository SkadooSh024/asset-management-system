package com.assetmanagement.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "assets")
public class Asset {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "asset_id")
        private Long assetId;

        @Column(name = "asset_code", nullable = false)
        private String assetCode;

        @Column(name = "asset_name", nullable = false)
        private String assetName;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id", nullable = false)
        private AssetCategory category;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "current_status_id", nullable = false)
        private AssetStatus currentStatus;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "owning_department_id")
        private Department owningDepartment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "current_department_id")
        private Department currentDepartment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_user_id")
        private User assignedUser;

        @Column(name = "brand")
        private String brand;

        @Column(name = "model")
        private String model;

        @Column(name = "serial_number")
        private String serialNumber;

        @Column(name = "asset_tag")
        private String assetTag;

        @Column(name = "purchase_date")
        private LocalDate purchaseDate;

        @Column(name = "warranty_expiry_date")
        private LocalDate warrantyExpiryDate;

        @Column(name = "purchase_cost")
        private BigDecimal purchaseCost;

        @Column(name = "specification_text")
        private String specificationText;

        @Column(name = "notes")
        private String notes;

        @Column(name = "image_url")
        private String imageUrl;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive;

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

    public Asset() {
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

    public AssetCategory getCategory() {
            return category;
        }

    public void setCategory(AssetCategory category) {
            this.category = category;
        }

    public AssetStatus getCurrentStatus() {
            return currentStatus;
        }

    public void setCurrentStatus(AssetStatus currentStatus) {
            this.currentStatus = currentStatus;
        }

    public Department getOwningDepartment() {
            return owningDepartment;
        }

    public void setOwningDepartment(Department owningDepartment) {
            this.owningDepartment = owningDepartment;
        }

    public Department getCurrentDepartment() {
            return currentDepartment;
        }

    public void setCurrentDepartment(Department currentDepartment) {
            this.currentDepartment = currentDepartment;
        }

    public User getAssignedUser() {
            return assignedUser;
        }

    public void setAssignedUser(User assignedUser) {
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
}
