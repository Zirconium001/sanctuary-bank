package com.sanctuary.controller;

import com.sanctuary.domain.transaction.TransactionRecord;
import com.sanctuary.dto.*;
import com.sanctuary.exception.BankingException;
import com.sanctuary.service.AccountService;
import com.sanctuary.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService txService;

    public TransactionController(AccountService accountService, TransactionService txService) {
        this.accountService = accountService;
        this.txService = txService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Void>> deposit(@Valid @RequestBody DepositRequest request) {
        try {
            accountService.deposit(request.getAccountNumber(), request.getAmount());
            return ResponseEntity.ok(ApiResponse.ok("Deposit successful", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@Valid @RequestBody WithdrawRequest request) {
        try {
            accountService.withdraw(request.getAccountNumber(), request.getPin(), request.getAmount());
            return ResponseEntity.ok(ApiResponse.ok("Withdrawal successful", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Void>> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            accountService.transfer(request.getFromAccount(), request.getPin(),
                request.getToAccount(), request.getAmount());
            return ResponseEntity.ok(ApiResponse.ok("Transfer successful", null));
        } catch (BankingException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionRecord>>> getHistory(@PathVariable String accountNumber) {
        List<TransactionRecord> txs = txService.getHistory(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Transaction history retrieved", txs));
    }

    @GetMapping("/history/{accountNumber}/recent")
    public ResponseEntity<ApiResponse<List<TransactionRecord>>> getRecentHistory(@PathVariable String accountNumber) {
        List<TransactionRecord> txs = txService.getHistory(accountNumber, 10);
        return ResponseEntity.ok(ApiResponse.ok("Recent transactions retrieved", txs));
    }
}
