package com.fsad.backend.controller;

import com.fsad.backend.dto.*;
import com.fsad.backend.model.User;
import com.fsad.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/teacher/signup")
    public ResponseEntity<?> teacherSignup(@RequestBody SignupRequest req) {
        try {
            return ResponseEntity.status(201).body(authService.teacherSignup(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/teacher/login")
    public ResponseEntity<?> teacherLogin(@RequestBody LoginRequest req) {
        try {
            return ResponseEntity.ok(authService.teacherLogin(req));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/teacher/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody OTPRequest req) {
        try {
            return ResponseEntity.ok(authService.verifyOTP(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/teacher/resend-otp")
    public ResponseEntity<?> resendOTP(@RequestBody OTPRequest req) {
        try {
            return ResponseEntity.ok(authService.resendOTP(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/student/signup")
    public ResponseEntity<?> studentSignup(@RequestBody SignupRequest req) {
        try {
            return ResponseEntity.status(201).body(authService.studentSignup(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest req) {
        try {
            return ResponseEntity.ok(authService.studentLogin(req));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully."));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("user", UserDTO.fromUser(user)));
    }
}
