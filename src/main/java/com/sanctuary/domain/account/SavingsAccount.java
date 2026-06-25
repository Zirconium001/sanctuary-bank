package com.sanctuary.domain.account;

import com.sanctuary.exception.*;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {
    private static final double DEFAULT_INTEREST_RATE = 0.035;
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double DAILY_WITHDRAWAL_LIMIT = 50_000.0;

    private double interestRate;
    private double minimumBalance;
    private double dailyWithdrawalLimit;

    public SavingsAccount(String customerId, String branchCode, int pin) {
        super(customerId, branchCode, AccountType.SAVINGS, pin);
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.minimumBalance = MINIMUM_BALANCE;
        this.dailyWithdrawalLimit = DAILY_WITHDRAWAL_LIMIT;
    }

    public SavingsAccount() {}

    public double getInterestRate() { return interestRate; }
    public double getMinimumBalance() { return minimumBalance; }
    public double getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }

    public void deposit(double amount) throws BankingException {
        validateActive();
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive.");
        setBalance(getBalance() + amount);
        touchLastTransaction();
    }

    public void withdraw(double amount) throws BankingException {
        validateActive();
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive.");
        if (amount > dailyWithdrawalLimit) throw new DailyLimitExceededException("withdrawal", dailyWithdrawalLimit);
        if (getBalance() - amount < minimumBalance)
            throw new InsufficientBalanceException(getBalance() - minimumBalance, amount);
        setBalance(getBalance() - amount);
        touchLastTransaction();
    }

    public double calculateMonthlyInterest() {
        return Math.round(getBalance() * interestRate / 12 * 100.0) / 100.0;
    }

    public void applyInterest() {
        double interest = calculateMonthlyInterest();
        setBalance(getBalance() + interest);
    }

    private void validateActive() throws BankingException {
        switch (getStatus()) {
            case CLOSED: throw new AccountNotFoundException(getAccountNumber());
            case FROZEN: throw new AccountFrozenException(getAccountNumber());
            case DORMANT: break;
            case ACTIVE: break;
        }
    }

    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public void setMinimumBalance(double minimumBalance) { this.minimumBalance = minimumBalance; }
    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) { this.dailyWithdrawalLimit = dailyWithdrawalLimit; }
}
