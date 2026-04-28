package com.fsad.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOTPEmail(String toEmail, String otp, String teacherName) throws Exception {
        String html = "<div style='font-family:Arial,sans-serif;max-width:500px;margin:0 auto;"
                + "padding:30px;border:1px solid #e0e0e0;border-radius:10px;'>"
                + "<h2 style='color:#4f46e5;text-align:center;'>FSAD Assignment System</h2>"
                + "<hr style='border:1px solid #e0e0e0;'/>"
                + "<p style='font-size:16px;'>Hi <strong>" + teacherName + "</strong>,</p>"
                + "<p style='font-size:15px;color:#333;'>Use the OTP below to complete your login:</p>"
                + "<div style='text-align:center;margin:30px 0;'>"
                + "<span style='font-size:40px;font-weight:bold;letter-spacing:10px;"
                + "color:#4f46e5;background:#f0f0ff;padding:15px 30px;"
                + "border-radius:10px;display:inline-block;'>" + otp + "</span>"
                + "</div>"
                + "<p style='font-size:14px;color:#666;text-align:center;'>"
                + "⏰ Valid for <strong>5 minutes</strong> only.</p>"
                + "<p style='font-size:14px;color:#999;text-align:center;'>"
                + "If you did not request this, please ignore this email.</p>"
                + "<hr style='border:1px solid #e0e0e0;'/>"
                + "<p style='font-size:12px;color:#aaa;text-align:center;'>"
                + "FSAD Assignment Management System</p></div>";

        String body = "{"
                + "\"sender\":{\"name\":\"FSAD System\",\"email\":\"" + fromEmail + "\"},"
                + "\"to\":[{\"email\":\"" + toEmail + "\",\"name\":\"" + teacherName + "\"}],"
                + "\"subject\":\"🔐 Your Login OTP - FSAD Assignment System\","
                + "\"htmlContent\":\"" + html.replace("\"", "\\\"") + "\""
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("accept", "application/json")
                .header("api-key", brevoApiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Brevo API response: {} - {}", response.statusCode(), response.body());

        if (response.statusCode() != 201) {
            throw new RuntimeException("Brevo API error: " + response.statusCode() + " - " + response.body());
        }
    }
}
