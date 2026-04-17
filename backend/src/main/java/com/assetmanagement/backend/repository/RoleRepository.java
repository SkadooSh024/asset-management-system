package com.assetmanagement.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleCode(String roleCode);
}
