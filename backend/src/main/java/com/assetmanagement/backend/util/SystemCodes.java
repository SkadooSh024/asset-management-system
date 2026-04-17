package com.assetmanagement.backend.util;

public final class SystemCodes {

    public static final String USER_STATUS_ACTIVE = "ACTIVE";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ASSET_STAFF = "ASSET_STAFF";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_END_USER = "END_USER";

    public static final String ASSET_STATUS_READY = "READY";
    public static final String ASSET_STATUS_IN_USE = "IN_USE";
    public static final String ASSET_STATUS_WAITING_MAINTENANCE = "WAITING_MAINTENANCE";
    public static final String ASSET_STATUS_IN_MAINTENANCE = "IN_MAINTENANCE";
    public static final String ASSET_STATUS_RETIRED = "RETIRED";

    public static final String ASSIGNMENT_STATUS_DRAFT = "DRAFT";
    public static final String ASSIGNMENT_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ASSIGNMENT_STATUS_COMPLETED = "COMPLETED";
    public static final String ASSIGNMENT_STATUS_CANCELED = "CANCELED";

    public static final String INCIDENT_STATUS_OPEN = "OPEN";
    public static final String INCIDENT_STATUS_IN_REVIEW = "IN_REVIEW";
    public static final String INCIDENT_STATUS_CONVERTED = "CONVERTED_TO_TICKET";
    public static final String INCIDENT_STATUS_RESOLVED = "RESOLVED";
    public static final String INCIDENT_STATUS_REJECTED = "REJECTED";
    public static final String INCIDENT_STATUS_CANCELED = "CANCELED";

    public static final String MAINTENANCE_STATUS_OPEN = "OPEN";
    public static final String MAINTENANCE_STATUS_ASSIGNED = "ASSIGNED";
    public static final String MAINTENANCE_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String MAINTENANCE_STATUS_COMPLETED = "COMPLETED";
    public static final String MAINTENANCE_STATUS_OUTSOURCED = "OUTSOURCED";

    private SystemCodes() {
    }
}
