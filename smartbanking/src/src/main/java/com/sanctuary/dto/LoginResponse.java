package com.sanctuary.dto;

public class LoginResponse {
    private String token;
    private String customerId;
    private String fullName;
    private String email;
    private String message;

    public LoginResponse(String token, String customerId, String fullName, String email) {
        this.token = token;
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.message = "Login successful";
    }

    public String getToken() { return token; }
    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
}
