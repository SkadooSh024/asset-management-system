package com.assetmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.IncidentReport;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {

    List<IncidentReport> findAllByOrderByCreatedAtDesc();

    long countByStatusIn(List<String> statuses);
}
