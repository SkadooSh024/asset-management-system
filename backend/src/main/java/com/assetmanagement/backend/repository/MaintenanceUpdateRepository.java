package com.assetmanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.MaintenanceUpdate;

public interface MaintenanceUpdateRepository extends JpaRepository<MaintenanceUpdate, Long> {
}
