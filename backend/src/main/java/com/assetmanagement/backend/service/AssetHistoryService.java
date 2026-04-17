package com.assetmanagement.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetmanagement.backend.dto.AssetHistoryResponse;
import com.assetmanagement.backend.entity.Asset;
import com.assetmanagement.backend.entity.AssetHistory;
import com.assetmanagement.backend.entity.AssetStatus;
import com.assetmanagement.backend.entity.Department;
import com.assetmanagement.backend.entity.User;
import com.assetmanagement.backend.repository.AssetHistoryRepository;
import com.assetmanagement.backend.util.ResponseMapper;

@Service
public class AssetHistoryService {

    private final AssetHistoryRepository assetHistoryRepository;

    public AssetHistoryService(AssetHistoryRepository assetHistoryRepository) {
        this.assetHistoryRepository = assetHistoryRepository;
    }

    @Transactional
    public void logHistory(
        Asset asset,
        String actionType,
        String referenceType,
        Long referenceId,
        AssetStatus fromStatus,
        AssetStatus toStatus,
        Department fromDepartment,
        Department toDepartment,
        User fromUser,
        User toUser,
        User actionByUser,
        String description
    ) {
        AssetHistory history = new AssetHistory();
        history.setAsset(asset);
        history.setActionType(actionType);
        history.setReferenceType(referenceType);
        history.setReferenceId(referenceId);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setFromDepartment(fromDepartment);
        history.setToDepartment(toDepartment);
        history.setFromUser(fromUser);
        history.setToUser(toUser);
        history.setActionByUser(actionByUser);
        history.setDescription(description);
        assetHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<AssetHistoryResponse> getAssetHistories(Long assetId) {
        return assetHistoryRepository.findAllByAsset_AssetIdOrderByActionTimeDesc(assetId)
            .stream()
            .map(ResponseMapper::toAssetHistoryResponse)
            .toList();
    }
}
