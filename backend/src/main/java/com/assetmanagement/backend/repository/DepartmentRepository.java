package com.assetmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByIsActiveTrueOrderByDepartmentNameAsc();
}
