package com.sanctuary.service;

import com.sanctuary.domain.account.Account;
import com.sanctuary.domain.account.AccountStatus;
import com.sanctuary.domain.customer.CustomerTier;
import com.sanctuary.domain.transaction.TransactionRecord;
import com.sanctuary.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplianceService {

    public static void checkDailyLimit(Account account, double amount, List<TransactionRecord> todayTxs) throws DailyLimitExceededException {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new DailyLimitExceededException("transaction", 0);
        }

        String customerId = account.getCustomerId();
        CustomerTier tier = CustomerTier.REGULAR;

        double withdrawalTotal = todayTxs.stream()
            .filter(t -> t.getDescription() != null && t.getDescription().contains("withdrawal"))
            .mapToDouble(TransactionRecord::getAmount)
            .sum();

        double transferOutTotal = todayTxs.stream()
            .filter(t -> t.getDescription() != null && t.getDescription().contains("Transfer"))
            .mapToDouble(TransactionRecord::getAmount)
            .sum();

        double totalOut = Math.abs(withdrawalTotal) + Math.abs(transferOutTotal);

        if (totalOut + amount > tier.dailyWithdrawalLimit()) {
            throw new DailyLimitExceededException("withdrawal/transfer", tier.dailyWithdrawalLimit());
        }

        if (amount > 1_000_000) {
            System.out.println("HIGH_VALUE_TX|" + account.getAccountNumber() + "|" + amount);
        }
    }

    public static void validateKycForTransaction(String kycStatus) throws ComplianceRejectionException {
        if (!"VERIFIED".equals(kycStatus)) {
            throw new ComplianceRejectionException("KYC not verified. Please complete KYC to proceed.");
        }
    }
}
