package com.sanctuary.controller;

import com.sanctuary.domain.account.*;
import com.sanctuary.dto.*;
import com.sanctuary.exception.BankingException;
import com.sanctuary.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/savings")
    public ResponseEntity<ApiResponse<SavingsAccount>> openSavings(@Valid @RequestBody OpenAccountRequest request) {
        try {
            SavingsAccount acc = accountService.openSavingsAccount(
                request.getCustomerId(), request.getBranchCode(),
                request.getPin(), request.getInitialDeposit(), request.getAccountName());
            return ResponseEntity.ok(ApiResponse.ok("Savings account opened", acc));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/checking")
    public ResponseEntity<ApiResponse<CheckingAccount>> openChecking(@Valid @RequestBody OpenAccountRequest request) {
        try {
            CheckingAccount acc = accountService.openCheckingAccount(
                request.getCustomerId(), request.getBranchCode(),
                request.getPin(), request.getInitialDeposit(), request.getAccountName());
            return ResponseEntity.ok(ApiResponse.ok("Checking account opened", acc));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<Account>> getAccount(@PathVariable String accountNumber) {
        try {
            Account acc = accountService.findAccount(accountNumber);
            return ResponseEntity.ok(ApiResponse.ok("Account retrieved", acc));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<String>> checkBalance(@PathVariable String accountNumber, @RequestBody Map<String, Integer> body) {
        try {
            String info = accountService.checkBalance(accountNumber, body.get("pin"));
            return ResponseEntity.ok(ApiResponse.ok("Balance retrieved", info));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{accountNumber}/pin")
    public ResponseEntity<ApiResponse<Void>> changePin(@PathVariable String accountNumber, @Valid @RequestBody ChangePinRequest request) {
        try {
            accountService.changePin(accountNumber, request.getOldPin(), request.getNewPin());
            return ResponseEntity.ok(ApiResponse.ok("PIN changed successfully", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<Void>> closeAccount(@PathVariable String accountNumber, @RequestBody Map<String, Integer> body) {
        try {
            accountService.closeAccount(accountNumber, body.get("pin"));
            return ResponseEntity.ok(ApiResponse.ok("Account closed", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/reopen")
    public ResponseEntity<ApiResponse<Void>> reopenAccount(@PathVariable String accountNumber, @RequestBody Map<String, Integer> body) {
        try {
            accountService.reopenAccount(accountNumber, body.get("pin"));
            return ResponseEntity.ok(ApiResponse.ok("Account reopened", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
