package com.assetmanagement.backend.dto;


public class AssetStatusResponse {

    private Long statusId;

        private String statusCode;

        private String statusName;

        private String statusGroup;

        private Boolean isAllocatable;

        private Integer sortOrder;

        private String description;

    public AssetStatusResponse() {
        }

    public AssetStatusResponse(Long statusId, String statusCode, String statusName, String statusGroup, Boolean isAllocatable, Integer sortOrder, String description) {
            this.statusId = statusId;
            this.statusCode = statusCode;
            this.statusName = statusName;
            this.statusGroup = statusGroup;
            this.isAllocatable = isAllocatable;
            this.sortOrder = sortOrder;
            this.description = description;
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

    public static AssetStatusResponseBuilder builder() {
            return new AssetStatusResponseBuilder();
        }

    public static class AssetStatusResponseBuilder {
            private Long statusId;
            private String statusCode;
            private String statusName;
            private String statusGroup;
            private Boolean isAllocatable;
            private Integer sortOrder;
            private String description;

            public AssetStatusResponseBuilder statusId(Long statusId) {
                this.statusId = statusId;
                return this;
            }

            public AssetStatusResponseBuilder statusCode(String statusCode) {
                this.statusCode = statusCode;
                return this;
            }

            public AssetStatusResponseBuilder statusName(String statusName) {
                this.statusName = statusName;
                return this;
            }

            public AssetStatusResponseBuilder statusGroup(String statusGroup) {
                this.statusGroup = statusGroup;
                return this;
            }

            public AssetStatusResponseBuilder isAllocatable(Boolean isAllocatable) {
                this.isAllocatable = isAllocatable;
                return this;
            }

            public AssetStatusResponseBuilder sortOrder(Integer sortOrder) {
                this.sortOrder = sortOrder;
                return this;
            }

            public AssetStatusResponseBuilder description(String description) {
                this.description = description;
                return this;
            }

            public AssetStatusResponse build() {
                return new AssetStatusResponse(statusId, statusCode, statusName, statusGroup, isAllocatable, sortOrder, description);
            }
        }
}
