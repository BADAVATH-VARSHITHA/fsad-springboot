package com.fsad.backend.dto;

public class OTPRequest {
    private String email;
    private String otp;

    public String getEmail()  { return email; }
    public String getOtp()    { return otp; }
    public void setEmail(String e) { this.email = e; }
    public void setOtp(String o)   { this.otp = o; }
}
