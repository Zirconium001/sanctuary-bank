package com.sanctuary.controller;

import com.sanctuary.domain.account.LoanAccount;
import com.sanctuary.dto.*;
import com.sanctuary.exception.BankingException;
import com.sanctuary.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<LoanAccount>> applyLoan(@Valid @RequestBody LoanApplicationRequest request) {
        try {
            LoanAccount loan = loanService.applyLoan(
                request.getCustomerId(), request.getBranchCode(),
                request.getPin(), request.getPrincipal(),
                request.getTenureMonths(), request.getLoanType());
            return ResponseEntity.ok(ApiResponse.ok("Loan approved and disbursed", loan));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/repay")
    public ResponseEntity<ApiResponse<Void>> repayLoan(@PathVariable String accountNumber, @RequestBody Map<String, Object> body) {
        try {
            int pin = Integer.parseInt(body.get("pin").toString());
            double amount = Double.parseDouble(body.get("amount").toString());
            loanService.repayLoan(accountNumber, pin, amount);
            return ResponseEntity.ok(ApiResponse.ok("Repayment processed", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
