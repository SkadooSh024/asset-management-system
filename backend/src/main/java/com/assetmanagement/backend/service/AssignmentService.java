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
        User actingUser = referenceDataService.requireUser(request.getActingUserId());
        Department targetDepartment = referenceDataService.getDepartmentOrNull(request.getTargetDepartmentId());
        User targetUser = referenceDataService.getUserOrNull(request.getTargetUserId());
        if (targetDepartment == null && targetUser == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Phieu cap phat phai co phong ban nhan hoac nguoi nhan.");
        }

        AssignmentForm form = new AssignmentForm();
        form.setFormCode(CodeGeneratorUtil.generateFormCode("ASF"));
        form.setAssignmentDate(request.getAssignmentDate());
        form.setSourceDepartment(referenceDataService.getDepartmentOrNull(request.getSourceDepartmentId()));
        form.setTargetDepartment(targetDepartment);
        form.setTargetUser(targetUser);
        form.setIssuedByUser(actingUser);
        form.setStatus(SystemCodes.ASSIGNMENT_STATUS_DRAFT);
        form.setReason(request.getReason());
        form.setNote(request.getNote());
        form.setCreatedBy(actingUser);
        form.setUpdatedBy(actingUser);

        Set<Long> uniqueAssetIds = new HashSet<>();
        List<AssignmentFormDetail> details = request.getDetails()
            .stream()
            .map(item -> {
                if (!uniqueAssetIds.add(item.getAssetId())) {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, "Phieu cap phat khong duoc chua tai san trung lap.");
                }

                Asset asset = referenceDataService.requireAsset(item.getAssetId());
                validateAssignableAsset(asset);

                AssignmentFormDetail detail = new AssignmentFormDetail();
                detail.setAssignmentForm(form);
                detail.setAsset(asset);
                detail.setExpectedReturnDate(item.getExpectedReturnDate());
                detail.setNote(item.getNote());
                return detail;
            })
            .toList();

        form.getDetails().clear();
        form.getDetails().addAll(details);
        return ResponseMapper.toAssignmentResponse(assignmentFormRepository.save(form));
    }

    @Transactional
    public AssignmentResponse approveAssignment(Long assignmentId, AssignmentActionRequest request) {
        AssignmentForm form = requireAssignment(assignmentId);
        if (!SystemCodes.ASSIGNMENT_STATUS_DRAFT.equals(form.getStatus())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Chi phieu dang o trang thai nhap moi co the phe duyet.");
        }

        User actingUser = referenceDataService.requireUser(request.getActingUserId());
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
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Chi phieu da duoc phe duyet moi co the hoan tat.");
        }

        User actingUser = referenceDataService.requireUser(request.getActingUserId());
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
                StringUtils.hasText(request.getNote()) ? request.getNote() : "Hoan tat cap phat tai san."
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
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Khong tim thay phieu cap phat."));
    }

    private void validateAssignableAsset(Asset asset) {
        if (!Boolean.TRUE.equals(asset.getIsActive())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Tai san khong con hoat dong de cap phat.");
        }
        if (!Boolean.TRUE.equals(asset.getCurrentStatus().getIsAllocatable())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Tai san " + asset.getAssetCode() + " hien khong the cap phat.");
        }
    }
}
