package com.sanctuary.controller;

import com.sanctuary.domain.account.FixedDepositAccount;
import com.sanctuary.domain.product.InterestRateCard;
import com.sanctuary.dto.*;
import com.sanctuary.exception.BankingException;
import com.sanctuary.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fd")
public class FdController {

    private final AccountService accountService;

    public FdController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<FixedDepositAccount>> createFd(@Valid @RequestBody FdCreateRequest request) {
        try {
            double rate = InterestRateCard.getFdRate(request.getTenureMonths());
            FixedDepositAccount fd = accountService.openFixedDeposit(
                request.getCustomerId(), request.getBranchCode(),
                request.getPin(), request.getAmount(), rate, request.getTenureMonths());
            return ResponseEntity.ok(ApiResponse.ok("Fixed Deposit created", fd));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/premature-close")
    public ResponseEntity<ApiResponse<Double>> prematureClose(@PathVariable String accountNumber) {
        try {
            FixedDepositAccount fd = (FixedDepositAccount) accountService.findAccount(accountNumber);
            double payout = fd.prematureWithdraw();
            accountService.findAccount(accountNumber); // forces save via repo
            return ResponseEntity.ok(ApiResponse.ok("FD prematurely closed", payout));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/mature")
    public ResponseEntity<ApiResponse<Double>> mature(@PathVariable String accountNumber) {
        try {
            FixedDepositAccount fd = (FixedDepositAccount) accountService.findAccount(accountNumber);
            if (!fd.isMatured()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("FD has not matured yet. Maturity date: " + fd.getMaturityDate()));
            }
            double payout = fd.closeMaturedDeposit();
            return ResponseEntity.ok(ApiResponse.ok("FD matured and credited", payout));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
