package com.fsad.backend.controller;

import com.fsad.backend.model.Assignment;
import com.fsad.backend.model.User;
import com.fsad.backend.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("assignments", assignmentService.getAll(user)));
    }

    @GetMapping("/student/my")
    public ResponseEntity<?> getMyAssignments(@AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.STUDENT)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        return ResponseEntity.ok(Map.of("assignments", assignmentService.getMyAssignments(user)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("assignment", assignmentService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body,
                                    @AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.TEACHER)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        try {
            Assignment assignment = assignmentService.create(body, user);
            return ResponseEntity.status(201).body(Map.of("assignment", assignment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Map<String, Object> body,
                                    @AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.TEACHER)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        try {
            return ResponseEntity.ok(Map.of("assignment", assignmentService.update(id, body, user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.TEACHER)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        try {
            assignmentService.delete(id, user);
            return ResponseEntity.ok(Map.of("message", "Assignment deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
