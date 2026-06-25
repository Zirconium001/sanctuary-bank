package com.sanctuary.service;

import com.sanctuary.domain.account.*;
import com.sanctuary.domain.customer.*;
import com.sanctuary.domain.transaction.*;
import com.sanctuary.exception.*;
import com.sanctuary.repository.*;
import com.sanctuary.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;
    private final TransactionRepository txRepo;

    public AccountService(AccountRepository accountRepo, CustomerRepository customerRepo, TransactionRepository txRepo) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.txRepo = txRepo;
    }

    public SavingsAccount openSavingsAccount(String customerId, String branchCode, int pin, double initialDeposit, String accountName) throws BankingException {
        Customer customer = customerRepo.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        if (customer.getKycStatus() != KycStatus.VERIFIED) {
            throw new ComplianceRejectionException("KYC verification required before opening an account.");
        }

        InputSanitizer.validatePin(pin);
        InputSanitizer.validatePositiveAmount(initialDeposit);
        if (initialDeposit < 500) throw new InvalidAmountException("Minimum initial deposit for Savings is 500.");

        SavingsAccount acc = new SavingsAccount(customerId, branchCode, pin);
        acc.setAccountName(accountName);
        acc.deposit(initialDeposit);
        accountRepo.save(acc);

        txRepo.save(new TransactionRecord(acc.getAccountNumber(), TransactionType.DEPOSIT, initialDeposit, 0, "Initial deposit for savings account opening"));
        return acc;
    }

    public CheckingAccount openCheckingAccount(String customerId, String branchCode, int pin, double initialDeposit, String accountName) throws BankingException {
        Customer customer = customerRepo.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        if (customer.getKycStatus() != KycStatus.VERIFIED) {
            throw new ComplianceRejectionException("KYC verification required before opening an account.");
        }

        InputSanitizer.validatePin(pin);
        InputSanitizer.validatePositiveAmount(initialDeposit);
        if (initialDeposit < 1000) throw new InvalidAmountException("Minimum initial deposit for Checking is 1,000.");

        CheckingAccount acc = new CheckingAccount(customerId, branchCode, pin);
        acc.setAccountName(accountName);
        acc.deposit(initialDeposit);
        accountRepo.save(acc);

        txRepo.save(new TransactionRecord(acc.getAccountNumber(), TransactionType.DEPOSIT, initialDeposit, 0, "Initial deposit for checking account opening"));
        return acc;
    }

    public FixedDepositAccount openFixedDeposit(String customerId, String branchCode, int pin, double amount, double rate, int tenureMonths) throws BankingException {
        Customer customer = customerRepo.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));

        InputSanitizer.validatePin(pin);
        InputSanitizer.validatePositiveAmount(amount);

        FixedDepositAccount fd = new FixedDepositAccount(customerId, branchCode, pin, amount, rate, tenureMonths);
        accountRepo.save(fd);

        txRepo.save(new TransactionRecord(fd.getAccountNumber(), TransactionType.FD_CREATION, amount, 0, "Fixed Deposit created for " + tenureMonths + " months at " + (rate * 100) + "% p.a."));
        return fd;
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        return accountRepo.findById(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public void verifyPin(Account account, int enteredPin) throws PinMismatchException, AccountFrozenException {
        if (account.isPinLocked()) {
            throw new AccountFrozenException(account.getAccountNumber());
        }
        if (!account.verifyPin(enteredPin)) {
            accountRepo.save(account);
            throw new PinMismatchException();
        }
        accountRepo.save(account);
    }

    public void deposit(String accountNumber, double amount) throws BankingException {
        Account acc = findAccount(accountNumber);
        InputSanitizer.validatePositiveAmount(amount);

        double before = acc.getBalance();
        if (acc instanceof SavingsAccount) {
            ((SavingsAccount) acc).deposit(amount);
        } else if (acc instanceof CheckingAccount) {
            ((CheckingAccount) acc).deposit(amount);
        } else {
            throw new InvalidAmountException("Deposits not supported for " + acc.getAccountType().display());
        }

        accountRepo.save(acc);
        txRepo.save(new TransactionRecord(accountNumber, TransactionType.DEPOSIT, amount, before, "Branch deposit"));
    }

    public void withdraw(String accountNumber, int pin, double amount) throws BankingException {
        Account acc = findAccount(accountNumber);
        verifyPin(acc, pin);
        InputSanitizer.validatePositiveAmount(amount);

        double before = acc.getBalance();
        if (acc instanceof SavingsAccount) {
            ((SavingsAccount) acc).withdraw(amount);
        } else if (acc instanceof CheckingAccount) {
            ((CheckingAccount) acc).withdraw(amount);
        } else {
            throw new InvalidAmountException("Withdrawals not supported for " + acc.getAccountType().display());
        }

        accountRepo.save(acc);
        txRepo.save(new TransactionRecord(accountNumber, TransactionType.WITHDRAWAL, -amount, before, "Mobile withdrawal"));
    }

    public void transfer(String fromAccount, int pin, String toAccount, double amount) throws BankingException {
        Account src = findAccount(fromAccount);
        verifyPin(src, pin);
        InputSanitizer.validatePositiveAmount(amount);

        Account dest = findAccount(toAccount);
        if (dest.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException(toAccount);
        }

        double beforeSrc = src.getBalance();
        double beforeDest = dest.getBalance();

        if (src instanceof SavingsAccount) {
            ((SavingsAccount) src).withdraw(amount);
        } else if (src instanceof CheckingAccount) {
            ((CheckingAccount) src).withdraw(amount);
        } else {
            throw new InvalidAmountException("Transfers not supported from " + src.getAccountType().display());
        }

        dest.setBalance(dest.getBalance() + amount);
        dest.touchLastTransaction();

        accountRepo.save(src);
        accountRepo.save(dest);

        txRepo.save(new TransactionRecord(fromAccount, TransactionType.TRANSFER_OUT, -amount, beforeSrc, "Transfer to " + toAccount));
        txRepo.save(new TransactionRecord(toAccount, TransactionType.TRANSFER_IN, amount, beforeDest, "Transfer from " + fromAccount));
    }

    public void closeAccount(String accountNumber, int pin) throws BankingException {
        Account acc = findAccount(accountNumber);
        verifyPin(acc, pin);
        acc.setStatus(AccountStatus.CLOSED);
        accountRepo.save(acc);
        txRepo.save(new TransactionRecord(accountNumber, TransactionType.ACCOUNT_CLOSURE, 0, acc.getBalance(), "Account closed by customer request"));
    }

    public void reopenAccount(String accountNumber, int pin) throws BankingException {
        Account acc = findAccount(accountNumber);
        if (acc.getStatus() != AccountStatus.CLOSED) {
            throw new BankingException("Only closed accounts can be reopened.");
        }
        if (!acc.verifyPin(pin)) throw new PinMismatchException();
        acc.setStatus(AccountStatus.ACTIVE);
        acc.resetPinLock();
        accountRepo.save(acc);
        txRepo.save(new TransactionRecord(accountNumber, TransactionType.ACCOUNT_REOPENED, 0, acc.getBalance(), "Account reopened by customer request"));
    }

    public void changePin(String accountNumber, int oldPin, int newPin) throws BankingException {
        Account acc = findAccount(accountNumber);
        if (!acc.verifyPin(oldPin)) throw new PinMismatchException();
        InputSanitizer.validatePin(newPin);
        acc.setPin(newPin);
        acc.resetPinLock();
        accountRepo.save(acc);
        txRepo.save(new TransactionRecord(accountNumber, TransactionType.PIN_CHANGE, 0, acc.getBalance(), "PIN changed"));
    }

    public String checkBalance(String accountNumber, int pin) throws BankingException {
        Account acc = findAccount(accountNumber);
        verifyPin(acc, pin);
        return "Account " + accountNumber + " | Balance: " + acc.getBalance() + " | Status: " + acc.getStatus().display();
    }
}
