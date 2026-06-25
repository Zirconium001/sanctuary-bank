package com.sanctuary.domain.account;

import com.sanctuary.exception.*;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {
    private static final double DEFAULT_OVERDRAFT_LIMIT = 10_000.0;
    private static final double MONTHLY_FEE = 150.0;
    private static final double DAILY_TRANSACTION_LIMIT = 100_000.0;

    private double overdraftLimit;
    private double monthlyFee;
    private double dailyTransactionLimit;

    public CheckingAccount(String customerId, String branchCode, int pin) {
        super(customerId, branchCode, AccountType.CHECKING, pin);
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
        this.monthlyFee = MONTHLY_FEE;
        this.dailyTransactionLimit = DAILY_TRANSACTION_LIMIT;
    }

    public CheckingAccount() {}

    public double getOverdraftLimit() { return overdraftLimit; }
    public double getAvailableBalance() { return getBalance() + overdraftLimit; }

    public void deposit(double amount) throws BankingException {
        validateActive();
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive.");
        setBalance(getBalance() + amount);
        touchLastTransaction();
    }

    public void withdraw(double amount) throws BankingException {
        validateActive();
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive.");
        if (amount > getAvailableBalance())
            throw new InsufficientBalanceException(getAvailableBalance(), amount);
        setBalance(getBalance() - amount);
        touchLastTransaction();
    }

    public void deductMonthlyFee() {
        setBalance(getBalance() - monthlyFee);
    }

    private void validateActive() throws BankingException {
        switch (getStatus()) {
            case CLOSED: throw new AccountNotFoundException(getAccountNumber());
            case FROZEN: throw new AccountFrozenException(getAccountNumber());
            case DORMANT: break;
            case ACTIVE: break;
        }
    }

    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }
    public void setMonthlyFee(double monthlyFee) { this.monthlyFee = monthlyFee; }
    public void setDailyTransactionLimit(double dailyTransactionLimit) { this.dailyTransactionLimit = dailyTransactionLimit; }
}
