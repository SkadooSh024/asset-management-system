package com.assetmanagement.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.LookupBundleResponse;
import com.assetmanagement.backend.service.ReferenceDataService;

@RestController
@RequestMapping("/api/lookups")
public class LookupController {

    private final ReferenceDataService referenceDataService;

    public LookupController(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }

    @GetMapping("/options")
    public LookupBundleResponse getOptions() {
        return referenceDataService.getLookupBundle();
    }
}
