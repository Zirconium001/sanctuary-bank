package com.sanctuary.controller;

import com.sanctuary.dto.ApiResponse;
import com.sanctuary.repository.AccountRepository;
import com.sanctuary.repository.CustomerRepository;
import com.sanctuary.repository.TransactionRepository;
import com.sanctuary.service.CustomerService;
import com.sanctuary.service.InterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final InterestService interestService;
    private final CustomerService customerService;

    public AdminController(CustomerRepository customerRepo, AccountRepository accountRepo,
                           TransactionRepository txRepo, InterestService interestService,
                           CustomerService customerService) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.interestService = interestService;
        this.customerService = customerService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = Map.of(
            "totalCustomers", customerRepo.count(),
            "totalAccounts", accountRepo.count(),
            "totalTransactions", txRepo.count()
        );
        return ResponseEntity.ok(ApiResponse.ok("System statistics", stats));
    }

    @PostMapping("/apply-interest")
    public ResponseEntity<ApiResponse<String>> applyInterest() {
        int count = interestService.applyMonthlySavingsInterest();
        return ResponseEntity.ok(ApiResponse.ok("Interest applied", count + " savings accounts updated"));
    }

    @PostMapping("/process-matured-fds")
    public ResponseEntity<ApiResponse<String>> processMaturedFds() {
        int count = interestService.processMaturedFixedDeposits();
        return ResponseEntity.ok(ApiResponse.ok("FDs processed", count + " matured FDs closed"));
    }

    @PostMapping("/upgrade-tiers")
    public ResponseEntity<ApiResponse<String>> upgradeTiers() {
        var customers = customerService.getAllCustomers();
        int upgraded = 0;
        for (var c : customers) {
            try {
                var oldTier = c.getTier();
                customerService.evaluateAndUpgradeTier(c.getCustomerId());
                var refreshed = customerService.findCustomer(c.getCustomerId());
                if (refreshed.getTier() != oldTier) upgraded++;
            } catch (Exception ignored) {}
        }
        return ResponseEntity.ok(ApiResponse.ok("Tiers upgraded", upgraded + " customers upgraded"));
    }
}
