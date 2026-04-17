package com.assetmanagement.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.assetmanagement.backend.dto.AssignmentActionRequest;
import com.assetmanagement.backend.dto.AssignmentCreateRequest;
import com.assetmanagement.backend.dto.AssignmentResponse;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.AssignmentForm;
import com.assetmanagement.backend.entity.AssignmentFormDetail;
import com.assetmanagement.backend.entity.Department;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.exception.BusinessException;
import com.assetmanagement.backend.repository.AssetRepository;
import com.assetmanagement.backend.repository.AssignmentFormRepository;
import com.assetmanagement.backend.util.CodeGeneratorUtil;
import com.assetmanagement.backend.util.ResponseMapper;
import com.assetmanagement.backend.util.SystemCodes;

@Service
public class AssignmentService {

    private final AssignmentFormRepository assignmentFormRepository;
    private final AssetRepository assetRepository;
    private final ReferenceDataService referenceDataService;
    private final AssetHistoryService assetHistoryService;

    public AssignmentService(
        AssignmentFormRepository assignmentFormRepository,
        AssetRepository assetRepository,
        ReferenceDataService referenceDataService,
        AssetHistoryService assetHistoryService
    ) {
        this.assignmentFormRepository = assignmentFormRepository;
        this.assetRepository = assetRepository;
        this.referenceDataService = referenceDataService;
        this.assetHistoryService = assetHistoryService;
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignments() {
        return assignmentFormRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(ResponseMapper::toAssignmentResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getAssignment(Long assignmentId) {
        return ResponseMapper.toAssignmentResponse(requireAssignment(assignmentId));
    }

    @Transactional
    public AssignmentResponse createAssignment(AssignmentCreateRequest request) {
        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ASSET_STAFF
        );
        User targetUser = referenceDataService.getUserOrNull(request.getTargetUserId());
        Department targetDepartment = referenceDataService.getDepartmentOrNull(request.getTargetDepartmentId());

        if (targetUser != null) {
            if (targetUser.getDepartment() == null) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Người nhận chưa được gán phòng ban.");
            }
            if (targetDepartment == null) {
                targetDepartment = targetUser.getDepartment();
            } else if (!targetDepartment.getDepartmentId().equals(targetUser.getDepartment().getDepartmentId())) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Người nhận phải thuộc đúng phòng ban nhận đã chọn.");
            }
        }

        if (targetDepartment == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Phiếu cấp phát phải có phòng ban nhận.");
        }

        AssignmentForm form = new AssignmentForm();
        form.setFormCode(CodeGeneratorUtil.generateFormCode("ASF"));
        form.setAssignmentDate(request.getAssignmentDate());
        form.setTargetDepartment(targetDepartment);
        form.setTargetUser(targetUser);
        form.setIssuedByUser(actingUser);
        form.setStatus(SystemCodes.ASSIGNMENT_STATUS_DRAFT);
        form.setReason(request.getReason());
        form.setNote(request.getNote());
        form.setCreatedBy(actingUser);
        form.setUpdatedBy(actingUser);

        Set<Long> uniqueAssetIds = new HashSet<>();
        Set<Long> sourceDepartmentIds = new HashSet<>();
        Department inferredSourceDepartment = null;
        List<AssignmentFormDetail> details = request.getDetails()
            .stream()
            .map(item -> {
                if (!uniqueAssetIds.add(item.getAssetId())) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, "Phiếu cấp phát không được chứa tài sản trùng lặp.");
                }

                Asset asset = referenceDataService.requireAsset(item.getAssetId());
                validateAssignableAsset(asset);

                Department sourceDepartment = asset.getCurrentDepartment() != null
                    ? asset.getCurrentDepartment()
                    : asset.getOwningDepartment();
                if (sourceDepartment != null) {
                    sourceDepartmentIds.add(sourceDepartment.getDepartmentId());
                }

                AssignmentFormDetail detail = new AssignmentFormDetail();
                detail.setAssignmentForm(form);
                detail.setAsset(asset);
                detail.setExpectedReturnDate(item.getExpectedReturnDate());
                detail.setNote(item.getNote());
                return detail;
            })
            .toList();

        if (request.getSourceDepartmentId() != null) {
            inferredSourceDepartment = referenceDataService.getDepartmentOrNull(request.getSourceDepartmentId());
        } else if (sourceDepartmentIds.size() == 1) {
            Asset firstAsset = details.getFirst().getAsset();
            inferredSourceDepartment = firstAsset.getCurrentDepartment() != null
                ? firstAsset.getCurrentDepartment()
                : firstAsset.getOwningDepartment();
        }

        form.setSourceDepartment(inferredSourceDepartment);
        form.getDetails().clear();
        form.getDetails().addAll(details);
        return ResponseMapper.toAssignmentResponse(assignmentFormRepository.save(form));
    }

    @Transactional
    public AssignmentResponse approveAssignment(Long assignmentId, AssignmentActionRequest request) {
        AssignmentForm form = requireAssignment(assignmentId);
        if (!SystemCodes.ASSIGNMENT_STATUS_DRAFT.equals(form.getStatus())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Chỉ phiếu đang ở trạng thái nháp mới có thể phê duyệt.");
        }

        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_MANAGER
        );
        form.setStatus(SystemCodes.ASSIGNMENT_STATUS_CONFIRMED);
        form.setApprovedByUser(actingUser);
        form.setUpdatedBy(actingUser);
        if (StringUtils.hasText(request.getNote())) {
            form.setNote(request.getNote());
        }

        return ResponseMapper.toAssignmentResponse(assignmentFormRepository.save(form));
    }

    @Transactional
    public AssignmentResponse completeAssignment(Long assignmentId, AssignmentActionRequest request) {
        AssignmentForm form = requireAssignment(assignmentId);
        if (!SystemCodes.ASSIGNMENT_STATUS_CONFIRMED.equals(form.getStatus())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Chỉ phiếu đã được phê duyệt mới có thể hoàn tất.");
        }

        User actingUser = referenceDataService.requireUserWithRoles(
            request.getActingUserId(),
            SystemCodes.ROLE_ASSET_STAFF
        );
        AssetStatus inUseStatus = referenceDataService.requireAssetStatusByCode(SystemCodes.ASSET_STATUS_IN_USE);
        Department destinationDepartment = form.getTargetDepartment() != null
            ? form.getTargetDepartment()
            : form.getTargetUser() != null ? form.getTargetUser().getDepartment() : null;

        for (AssignmentFormDetail detail : form.getDetails()) {
            Asset asset = detail.getAsset();
            validateAssignableAsset(asset);

            AssetStatus oldStatus = asset.getCurrentStatus();
            Department oldDepartment = asset.getCurrentDepartment();
            User oldUser = asset.getAssignedUser();

            asset.setCurrentStatus(inUseStatus);
            asset.setCurrentDepartment(destinationDepartment);
            asset.setAssignedUser(form.getTargetUser());
            asset.setUpdatedBy(actingUser);
            assetRepository.save(asset);

            assetHistoryService.logHistory(
                asset,
                "ASSIGNMENT",
                "assignment_forms",
                form.getAssignmentFormId(),
                oldStatus,
                inUseStatus,
                oldDepartment,
                destinationDepartment,
                oldUser,
                form.getTargetUser(),
                actingUser,
                StringUtils.hasText(request.getNote()) ? request.getNote() : "Hoàn tất cấp phát tài sản."
            );
        }

        form.setStatus(SystemCodes.ASSIGNMENT_STATUS_COMPLETED);
        form.setUpdatedBy(actingUser);
        if (StringUtils.hasText(request.getNote())) {
            form.setNote(request.getNote());
        }

        return ResponseMapper.toAssignmentResponse(assignmentFormRepository.save(form));
    }

    private AssignmentForm requireAssignment(Long assignmentId) {
        return assignmentFormRepository.findById(assignmentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Không tìm thấy phiếu cấp phát."));
    }

    private void validateAssignableAsset(Asset asset) {
        if (!Boolean.TRUE.equals(asset.getIsActive())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Tài sản không còn hoạt động để cấp phát.");
        }
        if (!Boolean.TRUE.equals(asset.getCurrentStatus().getIsAllocatable())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Tài sản " + asset.getAssetCode() + " hiện không thể cấp phát.");
        }
    }
}
