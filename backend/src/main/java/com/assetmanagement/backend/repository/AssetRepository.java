package com.assetmanagement.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findAllByOrderByUpdatedAtDesc();

    Optional<Asset> findByAssetCode(String assetCode);

    Optional<Asset> findBySerialNumber(String serialNumber);

    Optional<Asset> findByAssetTag(String assetTag);

    long countByIsActiveTrue();

    long countByCurrentStatus_StatusCode(String statusCode);
}
