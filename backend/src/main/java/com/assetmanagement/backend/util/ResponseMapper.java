package com.assetmanagement.backend.util;

import java.util.List;

import com.assetmanagement.backend.dto.AssetCategoryResponse;
import com.assetmanagement.backend.dto.AssetHistoryResponse;
import com.assetmanagement.backend.dto.AssetResponse;
import com.assetmanagement.backend.dto.AssetStatusResponse;
import com.assetmanagement.backend.dto.AssignmentDetailResponse;
import com.assetmanagement.backend.dto.AssignmentResponse;
import com.assetmanagement.backend.dto.IncidentResponse;
import com.assetmanagement.backend.dto.MaintenanceTicketResponse;
import com.assetmanagement.backend.dto.MaintenanceUpdateResponse;
import com.assetmanagement.backend.dto.ReferenceResponse;
import com.assetmanagement.backend.dto.UserReferenceResponse;
import com.assetmanagement.backend.dto.UserSessionResponse;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetCategory;
import com.assetmanagement.backend.entity.AssetHistory;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.AssignmentForm;
import com.assetmanagement.backend.entity.AssignmentFormDetail;
import com.assetmanagement.backend.entity.Department;
import com.assetmanagement.backend.entity.IncidentReport;
import com.assetmanagement.backend.entity.MaintenanceTicket;
import com.assetmanagement.backend.entity.MaintenanceUpdate;
import com.assetmanagement.backend.entity.User;

public final class ResponseMapper {

    private ResponseMapper() {
    }

    public static ReferenceResponse toReference(Department department) {
        if (department == null) {
            return null;
        }
        return ReferenceResponse.builder()
            .id(department.getDepartmentId())
            .code(department.getDepartmentCode())
            .name(department.getDepartmentName())
            .build();
    }

    public static ReferenceResponse toReference(AssetCategory category) {
        if (category == null) {
            return null;
        }
        return ReferenceResponse.builder()
            .id(category.getCategoryId())
            .code(category.getCategoryCode())
            .name(category.getCategoryName())
            .build();
    }

    public static ReferenceResponse toReference(AssetStatus status) {
        if (status == null) {
            return null;
        }
        return ReferenceResponse.builder()
            .id(status.getStatusId())
            .code(status.getStatusCode())
            .name(status.getStatusName())
            .build();
    }

    public static ReferenceResponse toAssetReference(Asset asset) {
        if (asset == null) {
            return null;
        }
        return ReferenceResponse.builder()
            .id(asset.getAssetId())
            .code(asset.getAssetCode())
            .name(asset.getAssetName())
            .build();
    }

    public static UserReferenceResponse toUserReference(User user) {
        if (user == null) {
            return null;
        }
        return UserReferenceResponse.builder()
            .id(user.getUserId())
            .username(user.getUsername())
            .fullName(user.getFullName())
            .roleCode(user.getRole() != null ? user.getRole().getRoleCode() : null)
            .departmentName(user.getDepartment() != null ? user.getDepartment().getDepartmentName() : null)
            .build();
    }

    public static UserSessionResponse toUserSession(User user) {
        return UserSessionResponse.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .status(user.getStatus())
            .roleCode(user.getRole() != null ? user.getRole().getRoleCode() : null)
            .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
            .departmentName(user.getDepartment() != null ? user.getDepartment().getDepartmentName() : null)
            .build();
    }

    public static AssetCategoryResponse toAssetCategoryResponse(AssetCategory category) {
        return AssetCategoryResponse.builder()
            .categoryId(category.getCategoryId())
            .categoryCode(category.getCategoryCode())
            .categoryName(category.getCategoryName())
            .defaultWarrantyMonths(category.getDefaultWarrantyMonths())
            .defaultMaintenanceCycleDays(category.getDefaultMaintenanceCycleDays())
            .description(category.getDescription())
            .isActive(category.getIsActive())
            .parentCategory(toReference(category.getParentCategory()))
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
    }

    public static AssetStatusResponse toAssetStatusResponse(AssetStatus status) {
        return AssetStatusResponse.builder()
            .statusId(status.getStatusId())
            .statusCode(status.getStatusCode())
            .statusName(status.getStatusName())
            .statusGroup(status.getStatusGroup())
            .isAllocatable(status.getIsAllocatable())
            .sortOrder(status.getSortOrder())
            .description(status.getDescription())
            .build();
    }

    public static AssetResponse toAssetResponse(Asset asset, List<AssetHistoryResponse> histories) {
        return AssetResponse.builder()
            .assetId(asset.getAssetId())
            .assetCode(asset.getAssetCode())
            .assetName(asset.getAssetName())
            .category(toReference(asset.getCategory()))
            .currentStatus(toReference(asset.getCurrentStatus()))
            .owningDepartment(toReference(asset.getOwningDepartment()))
            .currentDepartment(toReference(asset.getCurrentDepartment()))
            .assignedUser(toUserReference(asset.getAssignedUser()))
            .brand(asset.getBrand())
            .model(asset.getModel())
            .serialNumber(asset.getSerialNumber())
            .assetTag(asset.getAssetTag())
            .purchaseDate(asset.getPurchaseDate())
            .warrantyExpiryDate(asset.getWarrantyExpiryDate())
            .purchaseCost(asset.getPurchaseCost())
            .specificationText(asset.getSpecificationText())
            .notes(asset.getNotes())
            .imageUrl(asset.getImageUrl())
            .isActive(asset.getIsActive())
            .createdAt(asset.getCreatedAt())
            .updatedAt(asset.getUpdatedAt())
            .histories(histories)
            .build();
    }

    public static AssetHistoryResponse toAssetHistoryResponse(AssetHistory history) {
        return AssetHistoryResponse.builder()
            .assetHistoryId(history.getAssetHistoryId())
            .actionType(history.getActionType())
            .referenceType(history.getReferenceType())
            .referenceId(history.getReferenceId())
            .fromStatus(toReference(history.getFromStatus()))
            .toStatus(toReference(history.getToStatus()))
            .fromDepartment(toReference(history.getFromDepartment()))
            .toDepartment(toReference(history.getToDepartment()))
            .fromUser(toUserReference(history.getFromUser()))
            .toUser(toUserReference(history.getToUser()))
            .actionByUser(toUserReference(history.getActionByUser()))
            .description(history.getDescription())
            .actionTime(history.getActionTime())
            .build();
    }

    public static AssignmentResponse toAssignmentResponse(AssignmentForm form) {
        return AssignmentResponse.builder()
            .assignmentFormId(form.getAssignmentFormId())
            .formCode(form.getFormCode())
            .assignmentDate(form.getAssignmentDate())
            .status(form.getStatus())
            .reason(form.getReason())
            .note(form.getNote())
            .sourceDepartment(toReference(form.getSourceDepartment()))
            .targetDepartment(toReference(form.getTargetDepartment()))
            .targetUser(toUserReference(form.getTargetUser()))
            .issuedByUser(toUserReference(form.getIssuedByUser()))
            .approvedByUser(toUserReference(form.getApprovedByUser()))
            .details(
                form.getDetails()
                    .stream()
                    .map(ResponseMapper::toAssignmentDetailResponse)
                    .toList()
            )
            .createdAt(form.getCreatedAt())
            .updatedAt(form.getUpdatedAt())
            .build();
    }

    public static AssignmentDetailResponse toAssignmentDetailResponse(AssignmentFormDetail detail) {
        return AssignmentDetailResponse.builder()
            .assignmentFormDetailId(detail.getAssignmentFormDetailId())
            .asset(toAssetReference(detail.getAsset()))
            .expectedReturnDate(detail.getExpectedReturnDate())
            .note(detail.getNote())
            .build();
    }

    public static IncidentResponse toIncidentResponse(IncidentReport incident) {
        return IncidentResponse.builder()
            .incidentReportId(incident.getIncidentReportId())
            .reportCode(incident.getReportCode())
            .asset(toAssetReference(incident.getAsset()))
            .reportedByUser(toUserReference(incident.getReportedByUser()))
            .assignedToUser(toUserReference(incident.getAssignedToUser()))
            .reportDate(incident.getReportDate())
            .severity(incident.getSeverity())
            .issueTitle(incident.getIssueTitle())
            .issueDescription(incident.getIssueDescription())
            .status(incident.getStatus())
            .resolutionNote(incident.getResolutionNote())
            .createdAt(incident.getCreatedAt())
            .updatedAt(incident.getUpdatedAt())
            .build();
    }

    public static MaintenanceTicketResponse toMaintenanceTicketResponse(MaintenanceTicket ticket) {
        return MaintenanceTicketResponse.builder()
            .maintenanceTicketId(ticket.getMaintenanceTicketId())
            .ticketCode(ticket.getTicketCode())
            .incidentReportId(ticket.getIncidentReport() != null ? ticket.getIncidentReport().getIncidentReportId() : null)
            .asset(toAssetReference(ticket.getAsset()))
            .assignedToUser(toUserReference(ticket.getAssignedToUser()))
            .openedDate(ticket.getOpenedDate())
            .status(ticket.getStatus())
            .priority(ticket.getPriority())
            .maintenanceType(ticket.getMaintenanceType())
            .problemDescription(ticket.getProblemDescription())
            .externalServiceName(ticket.getExternalServiceName())
            .estimatedCost(ticket.getEstimatedCost())
            .actualCost(ticket.getActualCost())
            .completedDate(ticket.getCompletedDate())
            .resultSummary(ticket.getResultSummary())
            .updates(ticket.getUpdates().stream().map(ResponseMapper::toMaintenanceUpdateResponse).toList())
            .createdAt(ticket.getCreatedAt())
            .updatedAt(ticket.getUpdatedAt())
            .build();
    }

    public static MaintenanceUpdateResponse toMaintenanceUpdateResponse(MaintenanceUpdate update) {
        return MaintenanceUpdateResponse.builder()
            .maintenanceUpdateId(update.getMaintenanceUpdateId())
            .updateStatus(update.getUpdateStatus())
            .updateNote(update.getUpdateNote())
            .nextActionDate(update.getNextActionDate())
            .updatedByUser(toUserReference(update.getUpdatedByUser()))
            .updateTime(update.getUpdateTime())
            .build();
    }
}
