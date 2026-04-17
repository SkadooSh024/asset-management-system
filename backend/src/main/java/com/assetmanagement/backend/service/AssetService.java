package com.assetmanagement.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.assetmanagement.backend.dto.AssetRequest;
import com.assetmanagement.backend.dto.AssetResponse;
import com.assetmanagement.backend.dto.AssignmentActionRequest;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.Department;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.AssetRepository;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final ReferenceDataService referenceDataService;
    private final AssetHistoryService assetHistoryService;

    public AssetService(
        AssetRepository assetRepository,
        ReferenceDataService referenceDataService,
        AssetHistoryService assetHistoryService
    ) {
        this.assetRepository = assetRepository;
        this.referenceDataService = referenceDataService;
        this.assetHistoryService = assetHistoryService;
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> getAssets(String keyword, Long categoryId, Long statusId) {
        return assetRepository.findAllByOrderByUpdatedAtDesc()
            .stream()
            .filter(asset -> matchesKeyword(asset, keyword))
            .filter(asset -> categoryId == null || asset.getCategory().getCategoryId().equals(categoryId))
            .filter(asset -> statusId == null || asset.getCurrentStatus().getStatusId().equals(statusId))
            .map(asset -> ResponseMapper.toAssetResponse(asset, null))
            .toList();
    }

    @Transactional(readOnly = true)
    public AssetResponse getAsset(Long assetId) {
        Asset asset = referenceDataService.requireAsset(assetId);
        return ResponseMapper.toAssetResponse(asset, assetHistoryService.getAssetHistories(assetId));
    }

    @Transactional
    public AssetResponse createAsset(AssetRequest request) {
        validateAssetCode(request.getAssetCode(), null);

        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ADMIN,
            SystemCodes.ROLE_ASSET_STAFF
        );
        Asset asset = new Asset();
        applyAssetRequest(asset, request, actingUser, true);
        Asset savedAsset = assetRepository.save(asset);

        assetHistoryService.logHistory(
            savedAsset,
            "CREATE",
            "assets",
            savedAsset.getAssetId(),
            null,
            savedAsset.getCurrentStatus(),
            null,
            savedAsset.getCurrentDepartment(),
            null,
            savedAsset.getAssignedUser(),
            actingUser,
            "Khởi tạo tài sản mới trong hệ thống."
        );

        return ResponseMapper.toAssetResponse(savedAsset, assetHistoryService.getAssetHistories(savedAsset.getAssetId()));
    }

    @Transactional
    public AssetResponse updateAsset(Long assetId, AssetRequest request) {
        Asset asset = referenceDataService.requireAsset(assetId);
        validateAssetCode(request.getAssetCode(), assetId);

        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ADMIN,
            SystemCodes.ROLE_ASSET_STAFF
        );
        AssetStatus oldStatus = asset.getCurrentStatus();
        Department oldDepartment = asset.getCurrentDepartment();
        User oldAssignedUser = asset.getAssignedUser();

        applyAssetRequest(asset, request, actingUser, false);
        Asset savedAsset = assetRepository.save(asset);

        assetHistoryService.logHistory(
            savedAsset,
            "UPDATE",
            "assets",
            savedAsset.getAssetId(),
            oldStatus,
            savedAsset.getCurrentStatus(),
            oldDepartment,
            savedAsset.getCurrentDepartment(),
            oldAssignedUser,
            savedAsset.getAssignedUser(),
            actingUser,
            "Cập nhật thông tin tài sản."
        );

        return ResponseMapper.toAssetResponse(savedAsset, assetHistoryService.getAssetHistories(savedAsset.getAssetId()));
    }

    @Transactional
    public AssetResponse retireAsset(Long assetId, AssignmentActionRequest request) {
        Asset asset = referenceDataService.requireAsset(assetId);
        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ADMIN,
            SystemCodes.ROLE_ASSET_STAFF
        );
        AssetStatus retiredStatus = referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_RETIRED);

        AssetStatus oldStatus = asset.getCurrentStatus();
        Department oldDepartment = asset.getCurrentDepartment();
        User oldAssignedUser = asset.getAssignedUser();

        asset.setCurrentStatus(retiredStatus);
        asset.setAssignedUser(null);
        asset.setCurrentDepartment(asset.getOwningDepartment());
        asset.setIsActive(Boolean.FALSE);
        asset.setUpdatedBy(actingUser);
        Asset savedAsset = assetRepository.save(asset);

        assetHistoryService.logHistory(
            savedAsset,
            "RETIRE",
            "assets",
            savedAsset.getAssetId(),
            oldStatus,
            retiredStatus,
            oldDepartment,
            savedAsset.getCurrentDepartment(),
            oldAssignedUser,
            null,
            actingUser,
            StringUtils.hasText(request.getNote()) ? request.getNote() : "Đánh dấu thanh lý tài sản."
        );

        return ResponseMapper.toAssetResponse(savedAsset, assetHistoryService.getAssetHistories(savedAsset.getAssetId()));
    }

    private void applyAssetRequest(Asset asset, AssetRequest request, User actingUser, boolean isCreate) {
        asset.setAssetCode(request.getAssetCode().trim().toUpperCase());
        asset.setAssetName(request.getAssetName().trim());
        asset.setCategory(referenceDataService.requireCategory(request.getCategoryId()));

        AssetStatus status = referenceDataService.getAssetStatusOrNull(request.getCurrentStatusId());
        if (status == null) {
            status = request.getAssignedUserId() != null
                ? referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_IN_USE)
                : referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_READY);
        }
        asset.setCurrentStatus(status);
        asset.setOwningDepartment(referenceDataService.getDepartmentOrNull(request.getOwningDepartmentId()));
        asset.setCurrentDepartment(referenceDataService.getDepartmentOrNull(request.getCurrentDepartmentId()));
        asset.setAssignedUser(referenceDataService.getUserOrNull(request.getAssignedUserId()));
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setAssetTag(request.getAssetTag());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setWarrantyExpiryDate(request.getWarrantyExpiryDate());
        asset.setPurchaseCost(request.getPurchaseCost());
        asset.setSpecificationText(request.getSpecificationText());
        asset.setNotes(request.getNotes());
        asset.setImageUrl(request.getImageUrl());
        asset.setIsActive(request.getIsActive() == null ? Boolean.TRUE : request.getIsActive());
        if (isCreate) {
            asset.setCreatedBy(actingUser);
        }
        asset.setUpdatedBy(actingUser);
    }

    private boolean matchesKeyword(Asset asset, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return true;
        }
        String normalizedKeyword = keyword.trim().toLowerCase();
        return (asset.getAssetCode() != null && asset.getAssetCode().toLowerCase().contains(normalizedKeyword))
            || (asset.getAssetName() != null && asset.getAssetName().toLowerCase().contains(normalizedKeyword))
            || (asset.getBrand() != null && asset.getBrand().toLowerCase().contains(normalizedKeyword))
            || (asset.getModel() != null && asset.getModel().toLowerCase().contains(normalizedKeyword));
    }

    private void validateAssetCode(String assetCode, Long currentAssetId) {
        assetRepository.findByAssetCode(assetCode.trim().toUpperCase())
            .filter(existing -> !existing.getAssetId().equals(currentAssetId))
            .ifPresent(existing -> {
                throw new BusinessException(HttpStatus.CONFLICT, "Mã tài sản đã tồn tại.");
            });
    }
}
