package com.assetmanagement.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.AssetRequest;
import com.assetmanagement.backend.dto.AssetResponse;
import com.assetmanagement.backend.dto.AssignmentActionRequest;
import com.assetmanagement.backend.service.AssetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<AssetResponse> getAssets(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long statusId
    ) {
        return assetService.getAssets(keyword, categoryId, statusId);
    }

    @GetMapping("/{assetId}")
    public AssetResponse getAsset(@PathVariable Long assetId) {
        return assetService.getAsset(assetId);
    }

    @PostMapping
    public AssetResponse createAsset(@Valid @RequestBody AssetRequest request) {
        return assetService.createAsset(request);
    }

    @PutMapping("/{assetId}")
    public AssetResponse updateAsset(@PathVariable Long assetId, @Valid @RequestBody AssetRequest request) {
        return assetService.updateAsset(assetId, request);
    }

    @PatchMapping("/{assetId}/retire")
    public AssetResponse retireAsset(@PathVariable Long assetId, @Valid @RequestBody AssignmentActionRequest request) {
        return assetService.retireAsset(assetId, request);
    }
}
