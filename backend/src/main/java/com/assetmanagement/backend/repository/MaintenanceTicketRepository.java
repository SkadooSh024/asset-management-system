package com.assetmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.MaintenanceTicket;

public interface MaintenanceTicketRepository extends JpaRepository<MaintenanceTicket, Long> {

    List<MaintenanceTicket> findAllByOrderByCreatedAtDesc();

    long countByStatusNot(String status);

    boolean existsByIncidentReport_IncidentReportId(Long incidentReportId);
}
