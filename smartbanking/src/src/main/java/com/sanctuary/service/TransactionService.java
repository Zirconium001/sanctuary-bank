package com.sanctuary.service;

import com.sanctuary.domain.transaction.*;
import com.sanctuary.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository txRepo;

    public TransactionService(TransactionRepository txRepo) {
        this.txRepo = txRepo;
    }

    public List<TransactionRecord> getHistory(String accountNumber) {
        return txRepo.findByAccountNumberOrderByTimestampDesc(accountNumber);
    }

    public List<TransactionRecord> getHistory(String accountNumber, int limit) {
        return txRepo.findByAccountNumberOrderByTimestampDesc(accountNumber).stream().limit(limit).toList();
    }

    public List<TransactionRecord> getTodayTransactions() {
        return txRepo.findByTimestampStartingWith(java.time.LocalDate.now().toString());
    }

    public int getTransactionCount(String accountNumber) {
        return (int) txRepo.countByAccountNumber(accountNumber);
    }
}
