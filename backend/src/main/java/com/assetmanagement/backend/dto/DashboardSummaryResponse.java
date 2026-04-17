package com.assetmanagement.backend.dto;

import java.util.List;


public class DashboardSummaryResponse {

    private long totalAssets;

        private long readyAssets;

        private long assignedAssets;

        private long openIncidents;

        private long activeMaintenanceTickets;

        private long pendingAssignments;

        private List<AssignmentResponse> recentAssignments;

        private List<IncidentResponse> recentIncidents;

        private List<MaintenanceTicketResponse> recentMaintenanceTickets;

    public DashboardSummaryResponse() {
        }

    public DashboardSummaryResponse(long totalAssets, long readyAssets, long assignedAssets, long openIncidents, long activeMaintenanceTickets, long pendingAssignments, List<AssignmentResponse> recentAssignments, List<IncidentResponse> recentIncidents, List<MaintenanceTicketResponse> recentMaintenanceTickets) {
            this.totalAssets = totalAssets;
            this.readyAssets = readyAssets;
            this.assignedAssets = assignedAssets;
            this.openIncidents = openIncidents;
            this.activeMaintenanceTickets = activeMaintenanceTickets;
            this.pendingAssignments = pendingAssignments;
            this.recentAssignments = recentAssignments;
            this.recentIncidents = recentIncidents;
            this.recentMaintenanceTickets = recentMaintenanceTickets;
        }

    public long getTotalAssets() {
            return totalAssets;
        }

    public void setTotalAssets(long totalAssets) {
            this.totalAssets = totalAssets;
        }

    public long getReadyAssets() {
            return readyAssets;
        }

    public void setReadyAssets(long readyAssets) {
            this.readyAssets = readyAssets;
        }

    public long getAssignedAssets() {
            return assignedAssets;
        }

    public void setAssignedAssets(long assignedAssets) {
            this.assignedAssets = assignedAssets;
        }

    public long getOpenIncidents() {
            return openIncidents;
        }

    public void setOpenIncidents(long openIncidents) {
            this.openIncidents = openIncidents;
        }

    public long getActiveMaintenanceTickets() {
            return activeMaintenanceTickets;
        }

    public void setActiveMaintenanceTickets(long activeMaintenanceTickets) {
            this.activeMaintenanceTickets = activeMaintenanceTickets;
        }

    public long getPendingAssignments() {
            return pendingAssignments;
        }

    public void setPendingAssignments(long pendingAssignments) {
            this.pendingAssignments = pendingAssignments;
        }

    public List<AssignmentResponse> getRecentAssignments() {
            return recentAssignments;
        }

    public void setRecentAssignments(List<AssignmentResponse> recentAssignments) {
            this.recentAssignments = recentAssignments;
        }

    public List<IncidentResponse> getRecentIncidents() {
            return recentIncidents;
        }

    public void setRecentIncidents(List<IncidentResponse> recentIncidents) {
            this.recentIncidents = recentIncidents;
        }

    public List<MaintenanceTicketResponse> getRecentMaintenanceTickets() {
            return recentMaintenanceTickets;
        }

    public void setRecentMaintenanceTickets(List<MaintenanceTicketResponse> recentMaintenanceTickets) {
            this.recentMaintenanceTickets = recentMaintenanceTickets;
        }

    public static DashboardSummaryResponseBuilder builder() {
            return new DashboardSummaryResponseBuilder();
        }

    public static class DashboardSummaryResponseBuilder {
            private long totalAssets;
            private long readyAssets;
            private long assignedAssets;
            private long openIncidents;
            private long activeMaintenanceTickets;
            private long pendingAssignments;
            private List<AssignmentResponse> recentAssignments;
            private List<IncidentResponse> recentIncidents;
            private List<MaintenanceTicketResponse> recentMaintenanceTickets;

            public DashboardSummaryResponseBuilder totalAssets(long totalAssets) {
                this.totalAssets = totalAssets;
                return this;
            }

            public DashboardSummaryResponseBuilder readyAssets(long readyAssets) {
                this.readyAssets = readyAssets;
                return this;
            }

            public DashboardSummaryResponseBuilder assignedAssets(long assignedAssets) {
                this.assignedAssets = assignedAssets;
                return this;
            }

            public DashboardSummaryResponseBuilder openIncidents(long openIncidents) {
                this.openIncidents = openIncidents;
                return this;
            }

            public DashboardSummaryResponseBuilder activeMaintenanceTickets(long activeMaintenanceTickets) {
                this.activeMaintenanceTickets = activeMaintenanceTickets;
                return this;
            }

            public DashboardSummaryResponseBuilder pendingAssignments(long pendingAssignments) {
                this.pendingAssignments = pendingAssignments;
                return this;
            }

            public DashboardSummaryResponseBuilder recentAssignments(List<AssignmentResponse> recentAssignments) {
                this.recentAssignments = recentAssignments;
                return this;
            }

            public DashboardSummaryResponseBuilder recentIncidents(List<IncidentResponse> recentIncidents) {
                this.recentIncidents = recentIncidents;
                return this;
            }

            public DashboardSummaryResponseBuilder recentMaintenanceTickets(List<MaintenanceTicketResponse> recentMaintenanceTickets) {
                this.recentMaintenanceTickets = recentMaintenanceTickets;
                return this;
            }

            public DashboardSummaryResponse build() {
                return new DashboardSummaryResponse(totalAssets, readyAssets, assignedAssets, openIncidents, activeMaintenanceTickets, pendingAssignments, recentAssignments, recentIncidents, recentMaintenanceTickets);
            }
        }
}
