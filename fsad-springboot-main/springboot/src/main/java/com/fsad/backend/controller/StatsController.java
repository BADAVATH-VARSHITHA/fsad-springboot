package com.fsad.backend.controller;

import com.fsad.backend.model.User;
import com.fsad.backend.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> teacherStats(@AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.TEACHER)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        return ResponseEntity.ok(statsService.teacherStats(user));
    }

    @GetMapping("/student")
    public ResponseEntity<?> studentStats(@AuthenticationPrincipal User user) {
        if (user.getRole() != User.Role.STUDENT)
            return ResponseEntity.status(403).body(Map.of("message", "Access denied."));
        return ResponseEntity.ok(statsService.studentStats(user));
    }
}
