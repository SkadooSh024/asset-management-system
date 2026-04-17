package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AssetStatusRequest {

    @NotBlank(message = "Ma trang thai khong duoc de trong.")
        private String statusCode;

        @NotBlank(message = "Ten trang thai khong duoc de trong.")
        private String statusName;

        @NotBlank(message = "Nhom trang thai khong duoc de trong.")
        private String statusGroup;

        private Boolean isAllocatable;

        private Integer sortOrder;

        private String description;

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
