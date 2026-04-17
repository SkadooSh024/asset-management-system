package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssetStatusRequest {

    @NotNull(message = "Người thao tác không được để trống.")
        private Long actingUserId;

    @NotBlank(message = "Mã trạng thái không được để trống.")
        private String statusCode;

        @NotBlank(message = "Tên trạng thái không được để trống.")
        private String statusName;

        @NotBlank(message = "Nhóm trạng thái không được để trống.")
        private String statusGroup;

        private Boolean isAllocatable;

        private Integer sortOrder;

        private String description;

    public Long getActingUserId() {
            return actingUserId;
        }

    public void setActingUserId(Long actingUserId) {
            this.actingUserId = actingUserId;
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
