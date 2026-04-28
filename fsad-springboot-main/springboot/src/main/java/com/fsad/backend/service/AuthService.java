package com.fsad.backend.service;

import com.fsad.backend.dto.*;
import com.fsad.backend.model.*;
import com.fsad.backend.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository  userRepository;
    private final OTPRepository   otpRepository;
    private final JwtService      jwtService;
    private final EmailService    emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, OTPRepository otpRepository,
                       JwtService jwtService, EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.otpRepository   = otpRepository;
        this.jwtService      = jwtService;
        this.emailService    = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse teacherSignup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered.");

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail().toLowerCase())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.TEACHER)
                .department(req.getDepartment())
                .avatar(req.getName().substring(0, Math.min(2, req.getName().length())).toUpperCase())
                .joined(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user.getId());
        return AuthResponse.builder().token(token).user(UserDTO.fromUser(user)).build();
    }

    public AuthResponse teacherLogin(LoginRequest req) {
        User user = userRepository.findByEmailAndRole(req.getEmail(), User.Role.TEACHER)
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid email or password.");

        String otp = String.format("%06d", new Random().nextInt(1000000));
        otpRepository.deleteByEmail(req.getEmail());
        otpRepository.save(OTP.builder()
                .email(req.getEmail())
                .otp(passwordEncoder.encode(otp))
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build());

        try {
            emailService.sendOTPEmail(req.getEmail(), otp, user.getName());
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", req.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send OTP email. Check Gmail config. Error: " + e.getMessage());
        }

        return AuthResponse.builder()
                .requireOTP(true)
                .email(req.getEmail())
                .message("OTP sent to your email.")
                .build();
    }

    public AuthResponse verifyOTP(OTPRequest req) {
        OTP otpRecord = otpRepository
                .findTopByEmailAndUsedFalseOrderByCreatedAtDesc(req.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found. Please login again."));

        if (LocalDateTime.now().isAfter(otpRecord.getExpiresAt())) {
            otpRepository.delete(otpRecord);
            throw new RuntimeException("OTP has expired. Please login again.");
        }

        if (!passwordEncoder.matches(req.getOtp(), otpRecord.getOtp()))
            throw new RuntimeException("Invalid OTP. Please try again.");

        otpRepository.delete(otpRecord);

        User user = userRepository.findByEmailAndRole(req.getEmail(), User.Role.TEACHER)
                .orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtService.generateToken(user.getId());
        return AuthResponse.builder().token(token).user(UserDTO.fromUser(user)).build();
    }

    public AuthResponse resendOTP(OTPRequest req) {
        User user = userRepository.findByEmailAndRole(req.getEmail(), User.Role.TEACHER)
                .orElseThrow(() -> new RuntimeException("Teacher not found."));

        String otp = String.format("%06d", new Random().nextInt(1000000));
        otpRepository.deleteByEmail(req.getEmail());
        otpRepository.save(OTP.builder()
                .email(req.getEmail())
                .otp(passwordEncoder.encode(otp))
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build());

        try {
            emailService.sendOTPEmail(req.getEmail(), otp, user.getName());
        } catch (Exception e) {
            log.error("Failed to resend OTP email to {}: {}", req.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send OTP email. Error: " + e.getMessage());
        }

        return AuthResponse.builder().message("New OTP sent to your email.").build();
    }

    public AuthResponse studentSignup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered.");

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail().toLowerCase())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.STUDENT)
                .rollNo(req.getRollNo())
                .branch(req.getBranch())
                .semester(req.getSemester())
                .avatar(req.getName().substring(0, Math.min(2, req.getName().length())).toUpperCase())
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user.getId());
        return AuthResponse.builder().token(token).user(UserDTO.fromUser(user)).build();
    }

    public AuthResponse studentLogin(LoginRequest req) {
        User user = userRepository.findByEmailAndRole(req.getEmail(), User.Role.STUDENT)
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid email or password.");

        String token = jwtService.generateToken(user.getId());
        return AuthResponse.builder().token(token).user(UserDTO.fromUser(user)).build();
    }
}
