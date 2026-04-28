package com.fsad.backend.controller;

import com.fsad.backend.model.User;
import com.fsad.backend.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMySubmissions(@AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.STUDENT)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        return ResponseEntity.ok(Map.of("submissions", submissionService.getMySubmissions(user)));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getByAssignment(@PathVariable Long assignmentId,
                                             @AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.TEACHER)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        try {
            return ResponseEntity.ok(Map.of("submissions", submissionService.getByAssignment(assignmentId, user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> submit(@RequestParam("assignmentId") Long assignmentId,
                                    @RequestParam(value = "comment", required = false) String comment,
                                    @RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.STUDENT)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        try {
            return ResponseEntity.status(201)
                    .body(Map.of("submission", submissionService.submit(assignmentId, comment, file, user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<?> grade(@PathVariable Long id,
                                   @RequestBody Map<String, Object> body,
                                   @AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.TEACHER)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        try {
            return ResponseEntity.ok(Map.of("submission", submissionService.grade(id, body, user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("submission", submissionService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
