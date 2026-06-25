package com.sanctuary.controller;

import com.sanctuary.domain.customer.Customer;
import com.sanctuary.domain.customer.KycStatus;
import com.sanctuary.dto.*;
import com.sanctuary.exception.BankingException;
import com.sanctuary.security.JwtTokenProvider;
import com.sanctuary.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerService customerService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(CustomerService customerService, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            Customer customer = customerService.registerCustomer(
                request.getFullName(),
                request.getDateOfBirth(),
                request.getPhone(),
                request.getEmail(),
                encodedPassword
            );

            customerService.updateKycStatus(customer.getCustomerId(), KycStatus.VERIFIED);

            String token = tokenProvider.generateToken(customer.getCustomerId(), customer.getEmail());
            LoginResponse response = new LoginResponse(token, customer.getCustomerId(),
                customer.getFullName(), customer.getEmail());

            return ResponseEntity.ok(ApiResponse.ok("Registration successful", response));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Customer customer = customerService.findByEmail(request.getEmail());
        if (customer == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid email or password"));
        }

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid email or password"));
        }

        String token = tokenProvider.generateToken(customer.getCustomerId(), customer.getEmail());
        LoginResponse response = new LoginResponse(token, customer.getCustomerId(),
            customer.getFullName(), customer.getEmail());

        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }
}
