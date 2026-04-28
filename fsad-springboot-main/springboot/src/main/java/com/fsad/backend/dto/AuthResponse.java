package com.fsad.backend.dto;

public class AuthResponse {

    private String token;
    private UserDTO user;
    private boolean requireOTP;
    private String email;
    private String message;

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getToken()     { return token; }
    public UserDTO getUser()     { return user; }
    public boolean isRequireOTP() { return requireOTP; }
    public String getEmail()     { return email; }
    public String getMessage()   { return message; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setToken(String t)       { this.token = t; }
    public void setUser(UserDTO u)       { this.user = u; }
    public void setRequireOTP(boolean r) { this.requireOTP = r; }
    public void setEmail(String e)       { this.email = e; }
    public void setMessage(String m)     { this.message = m; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final AuthResponse r = new AuthResponse();
        public Builder token(String t)       { r.token = t; return this; }
        public Builder user(UserDTO u)       { r.user = u; return this; }
        public Builder requireOTP(boolean b) { r.requireOTP = b; return this; }
        public Builder email(String e)       { r.email = e; return this; }
        public Builder message(String m)     { r.message = m; return this; }
        public AuthResponse build()          { return r; }
    }
}
