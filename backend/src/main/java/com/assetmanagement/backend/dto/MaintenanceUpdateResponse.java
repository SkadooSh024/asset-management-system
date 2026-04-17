package com.assetmanagement.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class MaintenanceUpdateResponse {

    private Long maintenanceUpdateId;

        private String updateStatus;

        private String updateNote;

        private LocalDate nextActionDate;

        private UserReferenceResponse updatedByUser;

        private LocalDateTime updateTime;

    public MaintenanceUpdateResponse() {
        }

    public MaintenanceUpdateResponse(Long maintenanceUpdateId, String updateStatus, String updateNote, LocalDate nextActionDate, UserReferenceResponse updatedByUser, LocalDateTime updateTime) {
            this.maintenanceUpdateId = maintenanceUpdateId;
            this.updateStatus = updateStatus;
            this.updateNote = updateNote;
            this.nextActionDate = nextActionDate;
            this.updatedByUser = updatedByUser;
            this.updateTime = updateTime;
        }

    public Long getMaintenanceUpdateId() {
            return maintenanceUpdateId;
        }

    public void setMaintenanceUpdateId(Long maintenanceUpdateId) {
            this.maintenanceUpdateId = maintenanceUpdateId;
        }

    public String getUpdateStatus() {
            return updateStatus;
        }

    public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

    public String getUpdateNote() {
            return updateNote;
        }

    public void setUpdateNote(String updateNote) {
            this.updateNote = updateNote;
        }

    public LocalDate getNextActionDate() {
            return nextActionDate;
        }

    public void setNextActionDate(LocalDate nextActionDate) {
            this.nextActionDate = nextActionDate;
        }

    public UserReferenceResponse getUpdatedByUser() {
            return updatedByUser;
        }

    public void setUpdatedByUser(UserReferenceResponse updatedByUser) {
            this.updatedByUser = updatedByUser;
        }

    public LocalDateTime getUpdateTime() {
            return updateTime;
        }

    public void setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }

    public static MaintenanceUpdateResponseBuilder builder() {
            return new MaintenanceUpdateResponseBuilder();
        }

    public static class MaintenanceUpdateResponseBuilder {
            private Long maintenanceUpdateId;
            private String updateStatus;
            private String updateNote;
            private LocalDate nextActionDate;
            private UserReferenceResponse updatedByUser;
            private LocalDateTime updateTime;

            public MaintenanceUpdateResponseBuilder maintenanceUpdateId(Long maintenanceUpdateId) {
                this.maintenanceUpdateId = maintenanceUpdateId;
                return this;
            }

            public MaintenanceUpdateResponseBuilder updateStatus(String updateStatus) {
                this.updateStatus = updateStatus;
                return this;
            }

            public MaintenanceUpdateResponseBuilder updateNote(String updateNote) {
                this.updateNote = updateNote;
                return this;
            }

            public MaintenanceUpdateResponseBuilder nextActionDate(LocalDate nextActionDate) {
                this.nextActionDate = nextActionDate;
                return this;
            }

            public MaintenanceUpdateResponseBuilder updatedByUser(UserReferenceResponse updatedByUser) {
                this.updatedByUser = updatedByUser;
                return this;
            }

            public MaintenanceUpdateResponseBuilder updateTime(LocalDateTime updateTime) {
                this.updateTime = updateTime;
                return this;
            }

            public MaintenanceUpdateResponse build() {
                return new MaintenanceUpdateResponse(maintenanceUpdateId, updateStatus, updateNote, nextActionDate, updatedByUser, updateTime);
            }
        }
}
