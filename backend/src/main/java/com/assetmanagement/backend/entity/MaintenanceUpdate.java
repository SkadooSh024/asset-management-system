package com.assetmanagement.backend.entity;

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
@Table(name = "maintenance_updates")
public class MaintenanceUpdate {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "maintenance_update_id")
        private Long maintenanceUpdateId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "maintenance_ticket_id", nullable = false)
        private MaintenanceTicket maintenanceTicket;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "updated_by_user_id", nullable = false)
        private User updatedByUser;

        @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
        private LocalDateTime updateTime;

        @Column(name = "update_status")
        private String updateStatus;

        @Column(name = "update_note", nullable = false)
        private String updateNote;

        @Column(name = "next_action_date")
        private LocalDate nextActionDate;

    public MaintenanceUpdate() {
        }

    public Long getMaintenanceUpdateId() {
            return maintenanceUpdateId;
        }

    public void setMaintenanceUpdateId(Long maintenanceUpdateId) {
            this.maintenanceUpdateId = maintenanceUpdateId;
        }

    public MaintenanceTicket getMaintenanceTicket() {
            return maintenanceTicket;
        }

    public void setMaintenanceTicket(MaintenanceTicket maintenanceTicket) {
            this.maintenanceTicket = maintenanceTicket;
        }

    public User getUpdatedByUser() {
            return updatedByUser;
        }

    public void setUpdatedByUser(User updatedByUser) {
            this.updatedByUser = updatedByUser;
        }

    public LocalDateTime getUpdateTime() {
            return updateTime;
        }

    public void setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
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
}
