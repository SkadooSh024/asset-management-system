package com.assetmanagement.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assetmanagement.backend.dto.AssignmentActionRequest;
import com.assetmanagement.backend.dto.AssignmentCreateRequest;
import com.assetmanagement.backend.dto.AssignmentResponse;
import com.assetmanagement.backend.service.AssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public List<AssignmentResponse> getAssignments() {
        return assignmentService.getAssignments();
    }

    @GetMapping("/{assignmentId}")
    public AssignmentResponse getAssignment(@PathVariable Long assignmentId) {
        return assignmentService.getAssignment(assignmentId);
    }

    @PostMapping
    public AssignmentResponse createAssignment(@Valid @RequestBody AssignmentCreateRequest request) {
        return assignmentService.createAssignment(request);
    }

    @PatchMapping("/{assignmentId}/approve")
    public AssignmentResponse approveAssignment(@PathVariable Long assignmentId, @Valid @RequestBody AssignmentActionRequest request) {
        return assignmentService.approveAssignment(assignmentId, request);
    }

    @PatchMapping("/{assignmentId}/complete")
    public AssignmentResponse completeAssignment(@PathVariable Long assignmentId, @Valid @RequestBody AssignmentActionRequest request) {
        return assignmentService.completeAssignment(assignmentId, request);
    }
}
