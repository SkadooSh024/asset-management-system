package com.assetmanagement.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.IncidentAssignmentRequest;
import com.assetmanagement.backend.dto.IncidentCloseRequest;
import com.assetmanagement.backend.dto.IncidentConvertRequest;
import com.assetmanagement.backend.dto.IncidentReportRequest;
import com.assetmanagement.backend.dto.IncidentResponse;
import com.assetmanagement.backend.service.IncidentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<IncidentResponse> getIncidents() {
        return incidentService.getIncidents();
    }

    @GetMapping("/{incidentId}")
    public IncidentResponse getIncident(@PathVariable Long incidentId) {
        return incidentService.getIncident(incidentId);
    }

    @PostMapping
    public IncidentResponse createIncident(@Valid @RequestBody IncidentReportRequest request) {
        return incidentService.createIncident(request);
    }

    @PatchMapping("/{incidentId}/assign")
    public IncidentResponse assignIncident(@PathVariable Long incidentId, @Valid @RequestBody IncidentAssignmentRequest request) {
        return incidentService.assignIncident(incidentId, request);
    }

    @PatchMapping("/{incidentId}/close")
    public IncidentResponse closeIncident(@PathVariable Long incidentId, @Valid @RequestBody IncidentCloseRequest request) {
        return incidentService.closeIncident(incidentId, request);
    }

    @PostMapping("/{incidentId}/convert-to-maintenance")
    public IncidentResponse convertIncidentToMaintenance(
        @PathVariable Long incidentId,
        @Valid @RequestBody IncidentConvertRequest request
    ) {
        return incidentService.convertIncidentToMaintenance(incidentId, request);
    }
}
