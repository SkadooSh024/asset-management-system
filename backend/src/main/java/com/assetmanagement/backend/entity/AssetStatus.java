package com.assetmanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "asset_statuses")
public class AssetStatus {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "status_id")
        private Long statusId;

        @Column(name = "status_code", nullable = false)
        private String statusCode;

        @Column(name = "status_name", nullable = false)
        private String statusName;

        @Column(name = "status_group", nullable = false)
        private String statusGroup;

        @Column(name = "is_allocatable", nullable = false)
        private Boolean isAllocatable;

        @Column(name = "sort_order", nullable = false)
        private Integer sortOrder;

        @Column(name = "description")
        private String description;

    public AssetStatus() {
        }

    public Long getStatusId() {
            return statusId;
        }

    public void setStatusId(Long statusId) {
            this.statusId = statusId;
        }

    public String getStatusCode() {
            return statusCode;
        }

    public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

    public String getStatusName() {
            return statusName;
        }

    public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

    public String getStatusGroup() {
            return statusGroup;
        }

    public void setStatusGroup(String statusGroup) {
            this.statusGroup = statusGroup;
        }

    public Boolean getIsAllocatable() {
            return isAllocatable;
        }

    public void setIsAllocatable(Boolean isAllocatable) {
            this.isAllocatable = isAllocatable;
        }

    public Integer getSortOrder() {
            return sortOrder;
        }

    public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

    public String getDescription() {
            return description;
        }

    public void setDescription(String description) {
            this.description = description;
        }
}
