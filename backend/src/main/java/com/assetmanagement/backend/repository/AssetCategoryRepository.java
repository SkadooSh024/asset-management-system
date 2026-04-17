package com.assetmanagement.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assetmanagement.backend.entity.AssetCategory;

public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {

    List<AssetCategory> findAllByOrderByCategoryNameAsc();

    Optional<AssetCategory> findByCategoryCode(String categoryCode);
}
