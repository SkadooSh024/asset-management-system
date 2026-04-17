package com.assetmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.AssignmentForm;

public interface AssignmentFormRepository extends JpaRepository<AssignmentForm, Long> {

    List<AssignmentForm> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);
}
