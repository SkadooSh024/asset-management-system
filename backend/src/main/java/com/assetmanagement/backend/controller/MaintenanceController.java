package com.assetmanagement.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.MaintenanceCompleteRequest;
import com.assetmanagement.backend.dto.MaintenanceTicketRequest;
import com.assetmanagement.backend.dto.MaintenanceTicketResponse;
import com.assetmanagement.backend.dto.MaintenanceUpdateRequest;
import com.assetmanagement.backend.service.MaintenanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maintenance-tickets")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public List<MaintenanceTicketResponse> getTickets() {
        return maintenanceService.getTickets();
    }

    @GetMapping("/{ticketId}")
    public MaintenanceTicketResponse getTicket(@PathVariable Long ticketId) {
        return maintenanceService.getTicket(ticketId);
    }

    @PostMapping
    public MaintenanceTicketResponse createTicket(@Valid @RequestBody MaintenanceTicketRequest request) {
        return maintenanceService.createTicket(request);
    }

    @PostMapping("/{ticketId}/updates")
    public MaintenanceTicketResponse addUpdate(@PathVariable Long ticketId, @Valid @RequestBody MaintenanceUpdateRequest request) {
        return maintenanceService.addUpdate(ticketId, request);
    }

    @PatchMapping("/{ticketId}/complete")
    public MaintenanceTicketResponse completeTicket(@PathVariable Long ticketId, @Valid @RequestBody MaintenanceCompleteRequest request) {
        return maintenanceService.completeTicket(ticketId, request);
    }
}
