package com.assetmanagement.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.assetmanagement.backend.dto.IncidentAssignmentRequest;
import com.assetmanagement.backend.dto.IncidentCloseRequest;
import com.assetmanagement.backend.dto.IncidentConvertRequest;
import com.assetmanagement.backend.dto.IncidentResponse;
import com.assetmanagement.backend.dto.IncidentReportRequest;
import com.assetmanagement.backend.dto.MaintenanceTicketRequest;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.IncidentReport;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.AssetRepository;
import com.assetmanagement.backend.repository.IncidentReportRepository;
import com.assetmanagement.backend.repository.MaintenanceTicketRepository;
import com.assetmanagement.backend.util.CodeGeneratorUtil;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class IncidentService {

    private final IncidentReportRepository incidentReportRepository;
    private final MaintenanceTicketRepository maintenanceTicketRepository;
    private final AssetRepository assetRepository;
    private final ReferenceDataService referenceDataService;
    private final AssetHistoryService assetHistoryService;

    public IncidentService(
        IncidentReportRepository incidentReportRepository,
        MaintenanceTicketRepository maintenanceTicketRepository,
        AssetRepository assetRepository,
        ReferenceDataService referenceDataService,
        AssetHistoryService assetHistoryService
    ) {
        this.incidentReportRepository = incidentReportRepository;
        this.maintenanceTicketRepository = maintenanceTicketRepository;
        this.assetRepository = assetRepository;
        this.referenceDataService = referenceDataService;
        this.assetHistoryService = assetHistoryService;
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> getIncidents() {
        return incidentReportRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(ResponseMapper::toIncidentResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public IncidentResponse getIncident(Long incidentId) {
        return ResponseMapper.toIncidentResponse(referenceDataService.requireIncident(incidentId));
    }

    @Transactional
    public IncidentResponse createIncident(IncidentReportRequest request) {
        IncidentReport incident = new IncidentReport();
        Asset asset = referenceDataService.requireAsset(request.getAssetId());
        User reportedBy = referenceDataService.requireUserWithRoles(
            request.getReportedByUserId(),
            SystemCodes.ROLE_ASSET_STAFF,
            SystemCodes.ROLE_END_USER
        );
        User assignedTo = referenceDataService.getUserOrNull(request.getAssignedToUserId());
        AssetStatus waitingMaintenance = referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_WAITING_MAINTENANCE);

        incident.setReportCode(CodeGeneratorUtil.generateFormCode("INC"));
        incident.setAsset(asset);
        incident.setReportedByUser(reportedBy);
        incident.setAssignedToUser(assignedTo);
        incident.setSeverity(request.getSeverity().trim().toUpperCase());
        incident.setIssueTitle(request.getIssueTitle().trim());
        incident.setIssueDescription(request.getIssueDescription().trim());
        incident.setStatus(assignedTo != null ? SystemCodes.INCIDENT_STATUS_IN_REVIEW : SystemCodes.INCIDENT_STATUS_OPEN);

        IncidentReport savedIncident = incidentReportRepository.save(incident);

        AssetStatus oldStatus = asset.getCurrentStatus();
        asset.setCurrentStatus(waitingMaintenance);
        asset.setUpdatedBy(reportedBy);
        assetRepository.save(asset);

        assetHistoryService.logHistory(
            asset,
            "INCIDENT",
            "incident_reports",
            savedIncident.getIncidentReportId(),
            oldStatus,
            waitingMaintenance,
            asset.getCurrentDepartment(),
            asset.getCurrentDepartment(),
            asset.getAssignedUser(),
            asset.getAssignedUser(),
            reportedBy,
            "Ghi nhận báo hỏng / sự cố tài sản."
        );

        return ResponseMapper.toIncidentResponse(savedIncident);
    }

    @Transactional
    public IncidentResponse assignIncident(Long incidentId, IncidentAssignmentRequest request) {
        IncidentReport incident = referenceDataService.requireIncident(incidentId);
        referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ASSET_STAFF,
            SystemCodes.ROLE_MANAGER
        );
        if (
            !SystemCodes.INCIDENT_STATUS_OPEN.equals(incident.getStatus())
                && !SystemCodes.INCIDENT_STATUS_IN_REVIEW.equals(incident.getStatus())
        ) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Chỉ sự cố đang mở mới có thể phân công xử lý.");
        }

        User assignedTo = referenceDataService.requireUser(request.getAssignedToUserId());

        incident.setAssignedToUser(assignedTo);
        incident.setStatus(SystemCodes.INCIDENT_STATUS_IN_REVIEW);
        if (StringUtils.hasText(request.getNote())) {
            incident.setResolutionNote(request.getNote());
        }
        return ResponseMapper.toIncidentResponse(incidentReportRepository.save(incident));
    }

    @Transactional
    public IncidentResponse closeIncident(Long incidentId, IncidentCloseRequest request) {
        IncidentReport incident = referenceDataService.requireIncident(incidentId);
        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ASSET_STAFF,
            SystemCodes.ROLE_MANAGER
        );
        if (SystemCodes.INCIDENT_STATUS_CONVERTED.equals(incident.getStatus())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Sự cố đã được chuyển sang bảo trì, không thể đóng trực tiếp.");
        }

        String nextStatus = request.getStatus().trim().toUpperCase();
        if (
            !SystemCodes.INCIDENT_STATUS_RESOLVED.equals(nextStatus)
                && !SystemCodes.INCIDENT_STATUS_REJECTED.equals(nextStatus)
                && !SystemCodes.INCIDENT_STATUS_CANCELED.equals(nextStatus)
        ) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Trạng thái đóng sự cố không hợp lệ.");
        }

        incident.setStatus(nextStatus);
        incident.setResolutionNote(request.getResolutionNote());

        Asset asset = incident.getAsset();
        AssetStatus oldStatus = asset.getCurrentStatus();
        AssetStatus targetOperationalStatus = resolveOperationalStatus(asset);
        asset.setCurrentStatus(targetOperationalStatus);
        asset.setUpdatedBy(actingUser);
        assetRepository.save(asset);

        assetHistoryService.logHistory(
            asset,
            "INCIDENT_CLOSE",
            "incident_reports",
            incident.getIncidentReportId(),
            oldStatus,
            targetOperationalStatus,
            asset.getCurrentDepartment(),
            asset.getCurrentDepartment(),
            asset.getAssignedUser(),
            asset.getAssignedUser(),
            actingUser,
            StringUtils.hasText(request.getResolutionNote())
                ? request.getResolutionNote()
                : "Đóng sự cố và đưa tài sản về trạng thái vận hành."
        );

        return ResponseMapper.toIncidentResponse(incidentReportRepository.save(incident));
    }

    @Transactional
    public IncidentResponse convertIncidentToMaintenance(Long incidentId, IncidentConvertRequest request) {
        IncidentReport incident = referenceDataService.requireIncident(incidentId);
        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ASSET_STAFF,
            SystemCodes.ROLE_MANAGER
        );
        if (
            !SystemCodes.INCIDENT_STATUS_OPEN.equals(incident.getStatus())
                && !SystemCodes.INCIDENT_STATUS_IN_REVIEW.equals(incident.getStatus())
        ) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Chỉ sự cố đang mở mới có thể chuyển sang bảo trì.");
        }
        if (maintenanceTicketRepository.existsByIncidentReport_IncidentReportId(incidentId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Báo hỏng này đã được chuyển thành phiếu bảo trì.");
        }

        MaintenanceTicketRequest ticketRequest = new MaintenanceTicketRequest();
        ticketRequest.setActingUserId(request.getActingUserId());
        ticketRequest.setIncidentReportId(incidentId);
        ticketRequest.setAssetId(incident.getAsset().getAssetId());
        ticketRequest.setAssignedToUserId(request.getAssignedToUserId());
        ticketRequest.setPriority(request.getPriority());
        ticketRequest.setMaintenanceType(request.getMaintenanceType());
        ticketRequest.setProblemDescription(
            StringUtils.hasText(request.getProblemDescription()) ? request.getProblemDescription() : incident.getIssueDescription()
        );
        ticketRequest.setEstimatedCost(request.getEstimatedCost());

        incident.setStatus(SystemCodes.INCIDENT_STATUS_CONVERTED);
        incidentReportRepository.save(incident);

        new MaintenanceServiceAdapter(referenceDataService, assetRepository, maintenanceTicketRepository, assetHistoryService)
            .createTicketFromIncident(incident, actingUser, ticketRequest);

        return ResponseMapper.toIncidentResponse(incident);
    }

    private AssetStatus resolveOperationalStatus(Asset asset) {
        if (asset.getAssignedUser() != null) {
            return referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_IN_USE);
        }
        return referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_READY);
    }

    private static final class MaintenanceServiceAdapter {

        private final ReferenceDataService referenceDataService;
        private final AssetRepository assetRepository;
        private final MaintenanceTicketRepository maintenanceTicketRepository;
        private final AssetHistoryService assetHistoryService;

        private MaintenanceServiceAdapter(
            ReferenceDataService referenceDataService,
            AssetRepository assetRepository,
            MaintenanceTicketRepository maintenanceTicketRepository,
            AssetHistoryService assetHistoryService
        ) {
            this.referenceDataService = referenceDataService;
            this.assetRepository = assetRepository;
            this.maintenanceTicketRepository = maintenanceTicketRepository;
            this.assetHistoryService = assetHistoryService;
        }

        private void createTicketFromIncident(IncidentReport incident, User actingUser, MaintenanceTicketRequest request) {
            Asset asset = incident.getAsset();
            AssetStatus maintenanceStatus = referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_IN_MAINTENANCE);
            AssetStatus oldStatus = asset.getCurrentStatus();

            com.assetmanagement.backend.entity.MaintenanceTicket ticket = new com.assetmanagement.backend.entity.MaintenanceTicket();
            ticket.setTicketCode(CodeGeneratorUtil.generateFormCode("MNT"));
            ticket.setIncidentReport(incident);
            ticket.setAsset(asset);
            ticket.setAssignedToUser(referenceDataService.getUserOrNull(request.getAssignedToUserId()));
            ticket.setStatus(ticket.getAssignedToUser() != null ? SystemCodes.MAINTENANCE_STATUS_ASSIGNED : SystemCodes.MAINTENANCE_STATUS_OPEN);
            ticket.setPriority(request.getPriority().trim().toUpperCase());
            ticket.setMaintenanceType(request.getMaintenanceType().trim().toUpperCase());
            ticket.setProblemDescription(request.getProblemDescription().trim());
            ticket.setEstimatedCost(request.getEstimatedCost());
            ticket.setCreatedBy(actingUser);
            ticket.setUpdatedBy(actingUser);
            com.assetmanagement.backend.entity.MaintenanceTicket savedTicket = maintenanceTicketRepository.save(ticket);

            asset.setCurrentStatus(maintenanceStatus);
            asset.setUpdatedBy(actingUser);
            assetRepository.save(asset);

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
                "Chuyển báo hỏng thành phiếu bảo trì."
            );
        }
    }
}
