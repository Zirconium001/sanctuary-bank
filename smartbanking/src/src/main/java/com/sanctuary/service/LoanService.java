package com.sanctuary.service;

import com.sanctuary.domain.account.*;
import com.sanctuary.domain.customer.*;
import com.sanctuary.domain.product.InterestRateCard;
import com.sanctuary.domain.transaction.*;
import com.sanctuary.exception.*;
import com.sanctuary.repository.*;
import com.sanctuary.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoanService {
    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;
    private final TransactionRepository txRepo;

    public LoanService(AccountRepository accountRepo, CustomerRepository customerRepo, TransactionRepository txRepo) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.txRepo = txRepo;
    }

    public LoanAccount applyLoan(String customerId, String branchCode, int pin,
                                   double principal, int tenureMonths, String loanType) throws BankingException {
        Customer customer = customerRepo.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        if (customer.getKycStatus() != KycStatus.VERIFIED) {
            throw new ComplianceRejectionException("KYC verification required for loan application.");
        }

        InputSanitizer.validatePositiveAmount(principal);
        if (tenureMonths < 1 || tenureMonths > 240) {
            throw new InvalidAmountException("Loan tenure must be between 1 and 240 months.");
        }

        double rate = InterestRateCard.getLoanRate(loanType);
        LoanAccount loan = new LoanAccount(customerId, branchCode, pin, principal, rate, tenureMonths, loanType);
        accountRepo.save(loan);

        txRepo.save(new TransactionRecord(loan.getAccountNumber(), TransactionType.LOAN_DISBURSEMENT,
            principal, 0, "Loan disbursed - " + loanType + " - " + tenureMonths + " months @" + (rate * 100) + "%"));

        return loan;
    }

    public void repayLoan(String accountNumber, int pin, double amount) throws BankingException {
        Account acc = accountRepo.findById(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (!(acc instanceof LoanAccount)) {
            throw new AccountNotFoundException(accountNumber);
        }
        if (acc.isPinLocked()) throw new AccountFrozenException(accountNumber);
        if (!acc.verifyPin(pin)) {
            accountRepo.save(acc);
            throw new PinMismatchException();
        }

        LoanAccount loan = (LoanAccount) acc;
        double before = loan.getBalance();
        loan.makeRepayment(amount);
        accountRepo.save(loan);

        txRepo.save(new TransactionRecord(accountNumber, TransactionType.LOAN_REPAYMENT,
            -amount, before, "Loan repayment - EMI " + loan.getInstallmentsPaid() + "/" + loan.getTenureMonths()));
    }
}
