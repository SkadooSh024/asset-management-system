package com.assetmanagement.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.assetmanagement.backend.dto.MaintenanceCompleteRequest;
import com.assetmanagement.backend.dto.MaintenanceTicketRequest;
import com.assetmanagement.backend.dto.MaintenanceTicketResponse;
import com.assetmanagement.backend.dto.MaintenanceUpdateRequest;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.IncidentReport;
import com.assetmanagement.backend.entity.MaintenanceTicket;
import com.assetmanagement.backend.entity.MaintenanceUpdate;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.AssetRepository;
import com.assetmanagement.backend.repository.IncidentReportRepository;
import com.assetmanagement.backend.repository.MaintenanceTicketRepository;
import com.assetmanagement.backend.repository.MaintenanceUpdateRepository;
import com.assetmanagement.backend.util.CodeGeneratorUtil;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class MaintenanceService {

    private final MaintenanceTicketRepository maintenanceTicketRepository;
    private final MaintenanceUpdateRepository maintenanceUpdateRepository;
    private final IncidentReportRepository incidentReportRepository;
    private final AssetRepository assetRepository;
    private final ReferenceDataService referenceDataService;
    private final AssetHistoryService assetHistoryService;

    public MaintenanceService(
        MaintenanceTicketRepository maintenanceTicketRepository,
        MaintenanceUpdateRepository maintenanceUpdateRepository,
        IncidentReportRepository incidentReportRepository,
        AssetRepository assetRepository,
        ReferenceDataService referenceDataService,
        AssetHistoryService assetHistoryService
    ) {
        this.maintenanceTicketRepository = maintenanceTicketRepository;
        this.maintenanceUpdateRepository = maintenanceUpdateRepository;
        this.incidentReportRepository = incidentReportRepository;
        this.assetRepository = assetRepository;
        this.referenceDataService = referenceDataService;
        this.assetHistoryService = assetHistoryService;
    }

    @Transactional(readOnly = true)
    public List<MaintenanceTicketResponse> getTickets() {
        return maintenanceTicketRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(ResponseMapper::toMaintenanceTicketResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public MaintenanceTicketResponse getTicket(Long ticketId) {
        return ResponseMapper.toMaintenanceTicketResponse(requireTicket(ticketId));
    }

    @Transactional
    public MaintenanceTicketResponse createTicket(MaintenanceTicketRequest request) {
        User actingUser = referenceDataService.requireUser(request.getActingUserId());
        Asset asset = referenceDataService.requireAsset(request.getAssetId());
        IncidentReport incident = request.getIncidentReportId() == null
            ? null
            : referenceDataService.requireIncident(request.getIncidentReportId());

        if (incident != null && maintenanceTicketRepository.existsByIncidentReport_IncidentReportId(incident.getIncidentReportId())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Bao hong nay da co phieu bao tri.");
        }

        MaintenanceTicket ticket = new MaintenanceTicket();
        ticket.setTicketCode(CodeGeneratorUtil.generateFormCode("MNT"));
        ticket.setIncidentReport(incident);
        ticket.setAsset(asset);
        ticket.setAssignedToUser(referenceDataService.getUserOrNull(request.getAssignedToUserId()));
        ticket.setStatus(ticket.getAssignedToUser() != null ? SystemCodes.MAINTENANCE_STATUS_ASSIGNED : SystemCodes.MAINTENANCE_STATUS_OPEN);
        ticket.setPriority(request.getPriority().trim().toUpperCase());
        ticket.setMaintenanceType(request.getMaintenanceType().trim().toUpperCase());
        ticket.setProblemDescription(request.getProblemDescription().trim());
        ticket.setExternalServiceName(request.getExternalServiceName());
        ticket.setEstimatedCost(request.getEstimatedCost());
        ticket.setCreatedBy(actingUser);
        ticket.setUpdatedBy(actingUser);

        MaintenanceTicket savedTicket = maintenanceTicketRepository.save(ticket);

        AssetStatus maintenanceStatus = referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_IN_MAINTENANCE);
        AssetStatus oldStatus = asset.getCurrentStatus();
        asset.setCurrentStatus(maintenanceStatus);
        asset.setUpdatedBy(actingUser);
        assetRepository.save(asset);

        if (incident != null) {
            incident.setStatus(SystemCodes.INCIDENT_STATUS_CONVERTED);
            incidentReportRepository.save(incident);
        }

        assetHistoryService.logHistory(
            asset,
            "MAINTENANCE",
            "maintenance_tickets",
            savedTicket.getMaintenanceTicketId(),
            oldStatus,
            maintenanceStatus,
            asset.getCurrentDepartment(),
            asset.getCurrentDepartment(),
            asset.getAssignedUser(),
            asset.getAssignedUser(),
            actingUser,
            "Mo phieu bao tri tai san."
        );

        return ResponseMapper.toMaintenanceTicketResponse(savedTicket);
    }

    @Transactional
    public MaintenanceTicketResponse addUpdate(Long ticketId, MaintenanceUpdateRequest request) {
        MaintenanceTicket ticket = requireTicket(ticketId);
        User actingUser = referenceDataService.requireUser(request.getActingUserId());

        MaintenanceUpdate update = new MaintenanceUpdate();
        update.setMaintenanceTicket(ticket);
        update.setUpdatedByUser(actingUser);
        update.setUpdateStatus(request.getUpdateStatus());
        update.setUpdateNote(request.getUpdateNote().trim());
        update.setNextActionDate(request.getNextActionDate());
        maintenanceUpdateRepository.save(update);

        if (StringUtils.hasText(request.getUpdateStatus())) {
            ticket.setStatus(request.getUpdateStatus().trim().toUpperCase());
        }
        if (request.getActualCost() != null) {
            ticket.setActualCost(request.getActualCost());
        }
        if (StringUtils.hasText(request.getExternalServiceName())) {
            ticket.setExternalServiceName(request.getExternalServiceName().trim());
        }
        ticket.setUpdatedBy(actingUser);
        maintenanceTicketRepository.save(ticket);

        return ResponseMapper.toMaintenanceTicketResponse(ticket);
    }

    @Transactional
    public MaintenanceTicketResponse completeTicket(Long ticketId, MaintenanceCompleteRequest request) {
        MaintenanceTicket ticket = requireTicket(ticketId);
        User actingUser = referenceDataService.requireUser(request.getActingUserId());
        Asset asset = ticket.getAsset();

        AssetStatus targetStatus = request.getTargetStatusId() != null
            ? referenceDataService.requireAssetStatus(request.getTargetStatusId())
            : resolveOperationalStatus(asset);
        AssetStatus oldStatus = asset.getCurrentStatus();

        ticket.setStatus(SystemCodes.MAINTENANCE_STATUS_COMPLETED);
        ticket.setResultSummary(request.getResultSummary().trim());
        ticket.setCompletedDate(LocalDateTime.now());
        if (request.getActualCost() != null) {
            ticket.setActualCost(request.getActualCost());
        }
        ticket.setUpdatedBy(actingUser);
        maintenanceTicketRepository.save(ticket);

        asset.setCurrentStatus(targetStatus);
        asset.setUpdatedBy(actingUser);
        assetRepository.save(asset);

        if (ticket.getIncidentReport() != null) {
            IncidentReport incident = ticket.getIncidentReport();
            incident.setStatus(SystemCodes.INCIDENT_STATUS_RESOLVED);
            incident.setResolutionNote(request.getResultSummary());
            incidentReportRepository.save(incident);
        }

        assetHistoryService.logHistory(
            asset,
            "MAINTENANCE_COMPLETE",
            "maintenance_tickets",
            ticket.getMaintenanceTicketId(),
            oldStatus,
            targetStatus,
            asset.getCurrentDepartment(),
            asset.getCurrentDepartment(),
            asset.getAssignedUser(),
            asset.getAssignedUser(),
            actingUser,
            "Hoan tat bao tri tai san."
        );

        return ResponseMapper.toMaintenanceTicketResponse(ticket);
    }

    private AssetStatus resolveOperationalStatus(Asset asset) {
        if (asset.getAssignedUser() != null) {
            return referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_IN_USE);
        }
        return referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_READY);
    }

    private MaintenanceTicket requireTicket(Long ticketId) {
        return maintenanceTicketRepository.findById(ticketId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Khong tim thay phieu bao tri."));
    }
}
