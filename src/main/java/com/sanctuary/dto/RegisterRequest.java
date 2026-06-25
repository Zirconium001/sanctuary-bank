package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String dateOfBirth;
    @NotBlank @Pattern(regexp = "^[0-9]{7,15}$")
    private String phone;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, max = 128)
    private String password;

    public String getFullName() { return fullName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
