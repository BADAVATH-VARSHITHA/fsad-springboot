package com.fsad.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean used = false;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()                { return id; }
    public String getEmail()           { return email; }
    public String getOtp()             { return otp; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isUsed()            { return used; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)                   { this.id = id; }
    public void setEmail(String e)               { this.email = e; }
    public void setOtp(String o)                 { this.otp = o; }
    public void setExpiresAt(LocalDateTime e)    { this.expiresAt = e; }
    public void setUsed(boolean u)               { this.used = u; }
    public void setCreatedAt(LocalDateTime c)    { this.createdAt = c; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final OTP o = new OTP();
        public Builder email(String e)           { o.email = e; return this; }
        public Builder otp(String otp)           { o.otp = otp; return this; }
        public Builder expiresAt(LocalDateTime e) { o.expiresAt = e; return this; }
        public Builder used(boolean u)           { o.used = u; return this; }
        public OTP build()                       { return o; }
    }
}
