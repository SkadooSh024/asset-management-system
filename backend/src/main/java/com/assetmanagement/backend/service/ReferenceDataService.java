package com.assetmanagement.backend.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetmanagement.backend.dto.LookupBundleResponse;
import com.assetmanagement.backend.dto.ReferenceResponse;
import com.assetmanagement.backend.dto.UserReferenceResponse;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetCategory;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.Department;
import com.assetmanagement.backend.entity.IncidentReport;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.AssetCategoryRepository;
import com.assetmanagement.backend.repository.AssetRepository;
import com.assetmanagement.backend.repository.AssetStatusRepository;
import com.assetmanagement.backend.repository.DepartmentRepository;
import com.assetmanagement.backend.repository.IncidentReportRepository;
import com.assetmanagement.backend.repository.UserRepository;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
@Transactional(readOnly = true)
public class ReferenceDataService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetStatusRepository assetStatusRepository;
    private final AssetRepository assetRepository;
    private final IncidentReportRepository incidentReportRepository;

    public ReferenceDataService(
        DepartmentRepository departmentRepository,
        UserRepository userRepository,
        AssetCategoryRepository assetCategoryRepository,
        AssetStatusRepository assetStatusRepository,
        AssetRepository assetRepository,
        IncidentReportRepository incidentReportRepository
    ) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetStatusRepository = assetStatusRepository;
        this.assetRepository = assetRepository;
        this.incidentReportRepository = incidentReportRepository;
    }

    public LookupBundleResponse getLookupBundle() {
        List<ReferenceResponse> departments = departmentRepository.findAllByIsActiveTrueOrderByDepartmentNameAsc()
            .stream()
            .map(ResponseMapper::toReference)
            .toList();

        List<UserReferenceResponse> users = userRepository.findAllByStatusOrderByFullNameAsc(SystemCodes.USER_STATUS_ACTIVE)
            .stream()
            .map(ResponseMapper::toUserReference)
            .toList();

        List<ReferenceResponse> categories = assetCategoryRepository.findAllByOrderByCategoryNameAsc()
            .stream()
            .map(ResponseMapper::toReference)
            .toList();

        List<ReferenceResponse> statuses = assetStatusRepository.findAllByOrderBySortOrderAscStatusNameAsc()
            .stream()
            .map(ResponseMapper::toReference)
            .toList();

        return LookupBundleResponse.builder()
            .departments(departments)
            .users(users)
            .assetCategories(categories)
            .assetStatuses(statuses)
            .build();
    }

    public User requireUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng."));
    }

    public User requireUserWithRoles(Long userId, String... allowedRoleCodes) {
        User user = requireUser(userId);

        if (!SystemCodes.USER_STATUS_ACTIVE.equalsIgnoreCase(user.getStatus())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Tài khoản này hiện không hoạt động.");
        }

        if (allowedRoleCodes == null || allowedRoleCodes.length == 0) {
            return user;
        }

        String currentRoleCode = user.getRole() != null ? user.getRole().getRoleCode() : null;
        boolean isAllowed = Arrays.stream(allowedRoleCodes)
            .anyMatch(roleCode -> roleCode.equalsIgnoreCase(currentRoleCode));

        if (!isAllowed) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này.");
        }

        return user;
    }

    public User getUserOrNull(Long userId) {
        if (userId == null) {
            return null;
        }
        return requireUser(userId);
    }

    public Department getDepartmentOrNull(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng ban."));
    }

    public AssetCategory requireCategory(Long categoryId) {
        return assetCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục tài sản."));
    }

    public AssetCategory getCategoryOrNull(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return requireCategory(categoryId);
    }

    public AssetStatus requireAssetStatus(Long statusId) {
        return assetStatusRepository.findById(statusId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái tài sản."));
    }

    public AssetStatus getAssetStatusOrNull(Long statusId) {
        if (statusId == null) {
            return null;
        }
        return requireAssetStatus(statusId);
    }

    public AssetStatus requireAssetStatusByCode(String statusCode) {
        return assetStatusRepository.findByStatusCode(statusCode)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái tài sản " + statusCode + "."));
    }

    public Asset requireAsset(Long assetId) {
        return assetRepository.findById(assetId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy tài sản."));
    }

    public IncidentReport requireIncident(Long incidentReportId) {
        return incidentReportRepository.findById(incidentReportId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy báo hỏng / sự cố."));
    }
}
