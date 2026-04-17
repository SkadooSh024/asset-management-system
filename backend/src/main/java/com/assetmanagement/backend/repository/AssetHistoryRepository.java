package com.assetmanagement.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.AssetHistory;

public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

    List<AssetHistory> findAllByAsset_AssetIdOrderByActionTimeDesc(Long assetId);
}
