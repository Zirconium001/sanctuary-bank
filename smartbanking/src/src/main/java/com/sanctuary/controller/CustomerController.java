package com.sanctuary.controller;

import com.sanctuary.domain.account.Account;
import com.sanctuary.domain.customer.*;
import com.sanctuary.dto.ApiResponse;
import com.sanctuary.exception.BankingException;
import com.sanctuary.repository.AccountRepository;
import com.sanctuary.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final AccountRepository accountRepo;

    public CustomerController(CustomerService customerService, AccountRepository accountRepo) {
        this.customerService = customerService;
        this.accountRepo = accountRepo;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Customer>> getProfile(Authentication auth) {
        try {
            String customerId = (String) auth.getPrincipal();
            Customer customer = customerService.findCustomer(customerId);
            return ResponseEntity.ok(ApiResponse.ok("Profile retrieved", customer));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/me/address")
    public ResponseEntity<ApiResponse<Void>> updateAddress(Authentication auth, @RequestBody Map<String, String> address) {
        try {
            String customerId = (String) auth.getPrincipal();
            customerService.setAddress(customerId,
                address.get("addressLine1"),
                address.get("addressLine2"),
                address.get("city"),
                address.get("state"),
                address.get("postalCode"),
                address.getOrDefault("country", "India"));
            return ResponseEntity.ok(ApiResponse.ok("Address updated", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me/accounts")
    public ResponseEntity<ApiResponse<List<Account>>> getMyAccounts(Authentication auth) {
        String customerId = (String) auth.getPrincipal();
        List<Account> accounts = accountRepo.findByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.ok("Accounts retrieved", accounts));
    }
}
