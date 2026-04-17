package com.assetmanagement.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.AssetStatus;

public interface AssetStatusRepository extends JpaRepository<AssetStatus, Long> {

    List<AssetStatus> findAllByOrderBySortOrderAscStatusNameAsc();

    Optional<AssetStatus> findByStatusCode(String statusCode);
}
