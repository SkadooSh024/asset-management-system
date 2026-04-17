package com.assetmanagement.backend.entity;

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
@Table(name = "asset_histories")
public class AssetHistory {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "asset_history_id")
        private Long assetHistoryId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @Column(name = "action_type", nullable = false)
        private String actionType;

        @Column(name = "reference_type")
        private String referenceType;

        @Column(name = "reference_id")
        private Long referenceId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_status_id")
        private AssetStatus fromStatus;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_status_id")
        private AssetStatus toStatus;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_department_id")
        private Department fromDepartment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_department_id")
        private Department toDepartment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_user_id")
        private User fromUser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_user_id")
        private User toUser;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "action_by_user_id")
        private User actionByUser;

        @Column(name = "action_time", nullable = false, insertable = false, updatable = false)
        private LocalDateTime actionTime;

        @Column(name = "description")
        private String description;

    public AssetHistory() {
        }

    public Long getAssetHistoryId() {
            return assetHistoryId;
        }

    public void setAssetHistoryId(Long assetHistoryId) {
            this.assetHistoryId = assetHistoryId;
        }

    public Asset getAsset() {
            return asset;
        }

    public void setAsset(Asset asset) {
            this.asset = asset;
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

    public AssetStatus getFromStatus() {
            return fromStatus;
        }

    public void setFromStatus(AssetStatus fromStatus) {
            this.fromStatus = fromStatus;
        }

    public AssetStatus getToStatus() {
            return toStatus;
        }

    public void setToStatus(AssetStatus toStatus) {
            this.toStatus = toStatus;
        }

    public Department getFromDepartment() {
            return fromDepartment;
        }

    public void setFromDepartment(Department fromDepartment) {
            this.fromDepartment = fromDepartment;
        }

    public Department getToDepartment() {
            return toDepartment;
        }

    public void setToDepartment(Department toDepartment) {
            this.toDepartment = toDepartment;
        }

    public User getFromUser() {
            return fromUser;
        }

    public void setFromUser(User fromUser) {
            this.fromUser = fromUser;
        }

    public User getToUser() {
            return toUser;
        }

    public void setToUser(User toUser) {
            this.toUser = toUser;
        }

    public User getActionByUser() {
            return actionByUser;
        }

    public void setActionByUser(User actionByUser) {
            this.actionByUser = actionByUser;
        }

    public LocalDateTime getActionTime() {
            return actionTime;
        }

    public void setActionTime(LocalDateTime actionTime) {
            this.actionTime = actionTime;
        }

    public String getDescription() {
            return description;
        }

    public void setDescription(String description) {
            this.description = description;
        }
}
