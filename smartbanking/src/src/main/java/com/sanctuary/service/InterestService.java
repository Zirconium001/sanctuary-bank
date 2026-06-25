package com.sanctuary.service;

import com.sanctuary.domain.account.*;
import com.sanctuary.domain.transaction.*;
import com.sanctuary.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InterestService {
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;

    public InterestService(AccountRepository accountRepo, TransactionRepository txRepo) {
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
    }

    public int applyMonthlySavingsInterest() {
        int count = 0;
        List<Account> savingsAccounts = accountRepo.findByAccountType(AccountType.SAVINGS);
        for (Account acc : savingsAccounts) {
            if (acc.getStatus() != AccountStatus.ACTIVE) continue;
            SavingsAccount sa = (SavingsAccount) acc;
            double interest = sa.calculateMonthlyInterest();
            if (interest <= 0) continue;
            double before = sa.getBalance();
            sa.applyInterest();
            accountRepo.save(sa);
            txRepo.save(new TransactionRecord(sa.getAccountNumber(), TransactionType.INTEREST_CREDIT,
                interest, before, "Monthly interest credited @" + (sa.getInterestRate() * 100) + "% p.a."));
            count++;
        }
        return count;
    }

    public int processMaturedFixedDeposits() {
        int count = 0;
        List<Account> fdAccounts = accountRepo.findByAccountType(AccountType.FIXED_DEPOSIT);
        for (Account acc : fdAccounts) {
            if (acc.getStatus() != AccountStatus.ACTIVE) continue;
            FixedDepositAccount fd = (FixedDepositAccount) acc;
            if (!fd.isMatured()) continue;
            double payout = fd.closeMaturedDeposit();
            accountRepo.save(fd);
            txRepo.save(new TransactionRecord(fd.getAccountNumber(), TransactionType.FD_MATURITY,
                payout, fd.getBalance(), "Fixed Deposit matured - credited " + payout));
            count++;
        }
        return count;
    }
}
