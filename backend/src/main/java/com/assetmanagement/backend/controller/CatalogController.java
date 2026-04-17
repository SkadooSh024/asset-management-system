package com.assetmanagement.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.AssetCategoryRequest;
import com.assetmanagement.backend.dto.AssetCategoryResponse;
import com.assetmanagement.backend.dto.AssetStatusRequest;
import com.assetmanagement.backend.dto.AssetStatusResponse;
import com.assetmanagement.backend.dto.AssignmentActionRequest;
import com.assetmanagement.backend.service.CatalogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public List<AssetCategoryResponse> getCategories() {
        return catalogService.getCategories();
    }

    @PostMapping("/categories")
    public AssetCategoryResponse createCategory(@Valid @RequestBody AssetCategoryRequest request) {
        return catalogService.createCategory(request);
    }

    @PutMapping("/categories/{categoryId}")
    public AssetCategoryResponse updateCategory(@PathVariable Long categoryId, @Valid @RequestBody AssetCategoryRequest request) {
        return catalogService.updateCategory(categoryId, request);
    }

    @PatchMapping("/categories/{categoryId}/deactivate")
    public AssetCategoryResponse deactivateCategory(
        @PathVariable Long categoryId,
        @Valid @RequestBody AssignmentActionRequest request
    ) {
        return catalogService.deactivateCategory(categoryId, request);
    }

    @GetMapping("/statuses")
    public List<AssetStatusResponse> getStatuses() {
        return catalogService.getStatuses();
    }

    @PostMapping("/statuses")
    public AssetStatusResponse createStatus(@Valid @RequestBody AssetStatusRequest request) {
        return catalogService.createStatus(request);
    }

    @PutMapping("/statuses/{statusId}")
    public AssetStatusResponse updateStatus(@PathVariable Long statusId, @Valid @RequestBody AssetStatusRequest request) {
        return catalogService.updateStatus(statusId, request);
    }
}
