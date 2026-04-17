package com.assetmanagement.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.assetmanagement.backend.dto.AssetCategoryRequest;
import com.assetmanagement.backend.dto.AssetCategoryResponse;
import com.assetmanagement.backend.dto.AssetStatusRequest;
import com.assetmanagement.backend.dto.AssetStatusResponse;
import com.assetmanagement.backend.dto.AssignmentActionRequest;
import com.assetmanagement.backend.entity.AssetCategory;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.AssetCategoryRepository;
import com.assetmanagement.backend.repository.AssetStatusRepository;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class CatalogService {

    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetStatusRepository assetStatusRepository;
    private final ReferenceDataService referenceDataService;

    public CatalogService(
        AssetCategoryRepository assetCategoryRepository,
        AssetStatusRepository assetStatusRepository,
        ReferenceDataService referenceDataService
    ) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetStatusRepository = assetStatusRepository;
        this.referenceDataService = referenceDataService;
    }

    @Transactional(readOnly = true)
    public List<AssetCategoryResponse> getCategories() {
        return assetCategoryRepository.findAllByOrderByCategoryNameAsc()
            .stream()
            .map(ResponseMapper::toAssetCategoryResponse)
            .toList();
    }

    @Transactional
    public AssetCategoryResponse createCategory(AssetCategoryRequest request) {
        referenceDataService.requireUserWithRoles(request.getActingUserId(), SystemCodes.ROLE_ADMIN);
        validateCategoryCode(request.getCategoryCode(), null);

        AssetCategory category = new AssetCategory();
        applyCategoryRequest(category, request);
        return ResponseMapper.toAssetCategoryResponse(assetCategoryRepository.save(category));
    }

    @Transactional
    public AssetCategoryResponse updateCategory(Long categoryId, AssetCategoryRequest request) {
        referenceDataService.requireUserWithRoles(request.getActingUserId(), SystemCodes.ROLE_ADMIN);
        AssetCategory category = referenceDataService.requireCategory(categoryId);
        validateCategoryCode(request.getCategoryCode(), categoryId);

        applyCategoryRequest(category, request);
        return ResponseMapper.toAssetCategoryResponse(assetCategoryRepository.save(category));
    }

    @Transactional
    public AssetCategoryResponse deactivateCategory(Long categoryId, AssignmentActionRequest request) {
        referenceDataService.requireUserWithRoles(request.getActingUserId(), SystemCodes.ROLE_ADMIN);
        AssetCategory category = referenceDataService.requireCategory(categoryId);
        category.setIsActive(Boolean.FALSE);
        return ResponseMapper.toAssetCategoryResponse(assetCategoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<AssetStatusResponse> getStatuses() {
        return assetStatusRepository.findAllByOrderBySortOrderAscStatusNameAsc()
            .stream()
            .map(ResponseMapper::toAssetStatusResponse)
            .toList();
    }

    @Transactional
    public AssetStatusResponse createStatus(AssetStatusRequest request) {
        referenceDataService.requireUserWithRoles(request.getActingUserId(), SystemCodes.ROLE_ADMIN);
        validateStatusCode(request.getStatusCode(), null);

        AssetStatus status = new AssetStatus();
        applyStatusRequest(status, request);
        return ResponseMapper.toAssetStatusResponse(assetStatusRepository.save(status));
    }

    @Transactional
    public AssetStatusResponse updateStatus(Long statusId, AssetStatusRequest request) {
        referenceDataService.requireUserWithRoles(request.getActingUserId(), SystemCodes.ROLE_ADMIN);
        AssetStatus status = referenceDataService.requireAssetStatus(statusId);
        validateStatusCode(request.getStatusCode(), statusId);

        applyStatusRequest(status, request);
        return ResponseMapper.toAssetStatusResponse(assetStatusRepository.save(status));
    }

    private void applyCategoryRequest(AssetCategory category, AssetCategoryRequest request) {
        category.setParentCategory(referenceDataService.getCategoryOrNull(request.getParentCategoryId()));
        category.setCategoryCode(request.getCategoryCode().trim().toUpperCase());
        category.setCategoryName(request.getCategoryName().trim());
        category.setDefaultWarrantyMonths(request.getDefaultWarrantyMonths());
        category.setDefaultMaintenanceCycleDays(request.getDefaultMaintenanceCycleDays());
        category.setDescription(request.getDescription());
        category.setIsActive(request.getIsActive() == null ? Boolean.TRUE : request.getIsActive());
    }

    private void applyStatusRequest(AssetStatus status, AssetStatusRequest request) {
        status.setStatusCode(request.getStatusCode().trim().toUpperCase());
        status.setStatusName(request.getStatusName().trim());
        status.setStatusGroup(request.getStatusGroup().trim().toUpperCase());
        status.setIsAllocatable(request.getIsAllocatable() == null ? Boolean.FALSE : request.getIsAllocatable());
        status.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        status.setDescription(request.getDescription());
    }

    private void validateCategoryCode(String categoryCode, Long currentCategoryId) {
        if (!StringUtils.hasText(categoryCode)) {
            return;
        }
        assetCategoryRepository.findByCategoryCode(categoryCode.trim().toUpperCase())
            .filter(existing -> !existing.getCategoryId().equals(currentCategoryId))
            .ifPresent(existing -> {
                throw new BusinessException(HttpStatus.CONFLICT, "Mã danh mục tài sản đã tồn tại.");
            });
    }

    private void validateStatusCode(String statusCode, Long currentStatusId) {
        if (!StringUtils.hasText(statusCode)) {
            return;
        }
        assetStatusRepository.findByStatusCode(statusCode.trim().toUpperCase())
            .filter(existing -> !existing.getStatusId().equals(currentStatusId))
            .ifPresent(existing -> {
                throw new BusinessException(HttpStatus.CONFLICT, "Mã trạng thái tài sản đã tồn tại.");
            });
    }
}
