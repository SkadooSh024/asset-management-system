package com.assetmanagement.backend.dto;

import java.time.LocalDateTime;


public class AssetHistoryResponse {

    private Long assetHistoryId;

        private String actionType;

        private String referenceType;

        private Long referenceId;

        private ReferenceResponse fromStatus;

        private ReferenceResponse toStatus;

        private ReferenceResponse fromDepartment;

        private ReferenceResponse toDepartment;

        private UserReferenceResponse fromUser;

        private UserReferenceResponse toUser;

        private UserReferenceResponse actionByUser;

        private String description;

        private LocalDateTime actionTime;

    public AssetHistoryResponse() {
        }

    public AssetHistoryResponse(Long assetHistoryId, String actionType, String referenceType, Long referenceId, ReferenceResponse fromStatus, ReferenceResponse toStatus, ReferenceResponse fromDepartment, ReferenceResponse toDepartment, UserReferenceResponse fromUser, UserReferenceResponse toUser, UserReferenceResponse actionByUser, String description, LocalDateTime actionTime) {
            this.assetHistoryId = assetHistoryId;
            this.actionType = actionType;
            this.referenceType = referenceType;
            this.referenceId = referenceId;
            this.fromStatus = fromStatus;
            this.toStatus = toStatus;
            this.fromDepartment = fromDepartment;
            this.toDepartment = toDepartment;
            this.fromUser = fromUser;
            this.toUser = toUser;
            this.actionByUser = actionByUser;
            this.description = description;
            this.actionTime = actionTime;
        }

    public Long getAssetHistoryId() {
            return assetHistoryId;
        }

    public void setAssetHistoryId(Long assetHistoryId) {
            this.assetHistoryId = assetHistoryId;
        }

    public String getActionType() {
            return actionType;
        }

    public void setActionType(String actionType) {
            this.actionType = actionType;
        }

    public String getReferenceType() {
            return referenceType;
        }

    public void setReferenceType(String referenceType) {
            this.referenceType = referenceType;
        }

    public Long getReferenceId() {
            return referenceId;
        }

    public void setReferenceId(Long referenceId) {
            this.referenceId = referenceId;
        }

    public ReferenceResponse getFromStatus() {
            return fromStatus;
        }

    public void setFromStatus(ReferenceResponse fromStatus) {
            this.fromStatus = fromStatus;
        }

    public ReferenceResponse getToStatus() {
            return toStatus;
        }

    public void setToStatus(ReferenceResponse toStatus) {
            this.toStatus = toStatus;
        }

    public ReferenceResponse getFromDepartment() {
            return fromDepartment;
        }

    public void setFromDepartment(ReferenceResponse fromDepartment) {
            this.fromDepartment = fromDepartment;
        }

    public ReferenceResponse getToDepartment() {
            return toDepartment;
        }

    public void setToDepartment(ReferenceResponse toDepartment) {
            this.toDepartment = toDepartment;
        }

    public UserReferenceResponse getFromUser() {
            return fromUser;
        }

    public void setFromUser(UserReferenceResponse fromUser) {
            this.fromUser = fromUser;
        }

    public UserReferenceResponse getToUser() {
            return toUser;
        }

    public void setToUser(UserReferenceResponse toUser) {
            this.toUser = toUser;
        }

    public UserReferenceResponse getActionByUser() {
            return actionByUser;
        }

    public void setActionByUser(UserReferenceResponse actionByUser) {
            this.actionByUser = actionByUser;
        }

    public String getDescription() {
            return description;
        }

    public void setDescription(String description) {
            this.description = description;
        }

    public LocalDateTime getActionTime() {
            return actionTime;
        }

    public void setActionTime(LocalDateTime actionTime) {
            this.actionTime = actionTime;
        }

    public static AssetHistoryResponseBuilder builder() {
            return new AssetHistoryResponseBuilder();
        }

    public static class AssetHistoryResponseBuilder {
            private Long assetHistoryId;
            private String actionType;
            private String referenceType;
            private Long referenceId;
            private ReferenceResponse fromStatus;
            private ReferenceResponse toStatus;
            private ReferenceResponse fromDepartment;
            private ReferenceResponse toDepartment;
            private UserReferenceResponse fromUser;
            private UserReferenceResponse toUser;
            private UserReferenceResponse actionByUser;
            private String description;
            private LocalDateTime actionTime;

            public AssetHistoryResponseBuilder assetHistoryId(Long assetHistoryId) {
                this.assetHistoryId = assetHistoryId;
                return this;
            }

            public AssetHistoryResponseBuilder actionType(String actionType) {
                this.actionType = actionType;
                return this;
            }

            public AssetHistoryResponseBuilder referenceType(String referenceType) {
                this.referenceType = referenceType;
                return this;
            }

            public AssetHistoryResponseBuilder referenceId(Long referenceId) {
                this.referenceId = referenceId;
                return this;
            }

            public AssetHistoryResponseBuilder fromStatus(ReferenceResponse fromStatus) {
                this.fromStatus = fromStatus;
                return this;
            }

            public AssetHistoryResponseBuilder toStatus(ReferenceResponse toStatus) {
                this.toStatus = toStatus;
                return this;
            }

            public AssetHistoryResponseBuilder fromDepartment(ReferenceResponse fromDepartment) {
                this.fromDepartment = fromDepartment;
                return this;
            }

            public AssetHistoryResponseBuilder toDepartment(ReferenceResponse toDepartment) {
                this.toDepartment = toDepartment;
                return this;
            }

            public AssetHistoryResponseBuilder fromUser(UserReferenceResponse fromUser) {
                this.fromUser = fromUser;
                return this;
            }

            public AssetHistoryResponseBuilder toUser(UserReferenceResponse toUser) {
                this.toUser = toUser;
                return this;
            }

            public AssetHistoryResponseBuilder actionByUser(UserReferenceResponse actionByUser) {
                this.actionByUser = actionByUser;
                return this;
            }

            public AssetHistoryResponseBuilder description(String description) {
                this.description = description;
                return this;
            }

            public AssetHistoryResponseBuilder actionTime(LocalDateTime actionTime) {
                this.actionTime = actionTime;
                return this;
            }

            public AssetHistoryResponse build() {
                return new AssetHistoryResponse(assetHistoryId, actionType, referenceType, referenceId, fromStatus, toStatus, fromDepartment, toDepartment, fromUser, toUser, actionByUser, description, actionTime);
            }
        }
}
