package com.assetmanagement.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetmanagement.backend.dto.DashboardSummaryResponse;
import com.assetmanagement.backend.repository.AssetRepository;
import com.assetmanagement.backend.repository.AssignmentFormRepository;
import com.assetmanagement.backend.repository.IncidentReportRepository;
import com.assetmanagement.backend.repository.MaintenanceTicketRepository;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class DashboardService {

    private final AssetRepository assetRepository;
    private final AssignmentFormRepository assignmentFormRepository;
    private final IncidentReportRepository incidentReportRepository;
    private final MaintenanceTicketRepository maintenanceTicketRepository;

    public DashboardService(
        AssetRepository assetRepository,
        AssignmentFormRepository assignmentFormRepository,
        IncidentReportRepository incidentReportRepository,
        MaintenanceTicketRepository maintenanceTicketRepository
    ) {
        this.assetRepository = assetRepository;
        this.assignmentFormRepository = assignmentFormRepository;
        this.incidentReportRepository = incidentReportRepository;
        this.maintenanceTicketRepository = maintenanceTicketRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        return DashboardSummaryResponse.builder()
            .totalAssets(assetRepository.countByIsActiveTrue())
            .readyAssets(assetRepository.countByCurrentStatus_StatusCode(SystemCodes.ASSET_STATUS_READY))
            .assignedAssets(assetRepository.countByCurrentStatus_StatusCode(SystemCodes.ASSET_STATUS_IN_USE))
            .openIncidents(
                incidentReportRepository.countByStatusIn(
                    List.of(SystemCodes.INCIDENT_STATUS_OPEN, SystemCodes.INCIDENT_STATUS_IN_REVIEW)
                )
            )
            .activeMaintenanceTickets(maintenanceTicketRepository.countByStatusNot(SystemCodes.MAINTENANCE_STATUS_COMPLETED))
            .pendingAssignments(assignmentFormRepository.countByStatus(SystemCodes.ASSIGNMENT_STATUS_DRAFT))
            .recentAssignments(
                assignmentFormRepository.findAllByOrderByCreatedAtDesc()
                    .stream()
                    .limit(5)
                    .map(ResponseMapper::toAssignmentResponse)
                    .toList()
            )
            .recentIncidents(
                incidentReportRepository.findAllByOrderByCreatedAtDesc()
                    .stream()
                    .limit(5)
                    .map(ResponseMapper::toIncidentResponse)
                    .toList()
            )
            .recentMaintenanceTickets(
                maintenanceTicketRepository.findAllByOrderByCreatedAtDesc()
                    .stream()
                    .limit(5)
                    .map(ResponseMapper::toMaintenanceTicketResponse)
                    .toList()
            )
            .build();
    }
}
