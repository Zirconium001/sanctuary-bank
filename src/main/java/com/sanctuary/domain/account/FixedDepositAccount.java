package com.sanctuary.domain.account;

import com.sanctuary.exception.*;
import com.sanctuary.util.DateUtils;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("FIXED_DEPOSIT")
public class FixedDepositAccount extends Account {
    private static final double PENALTY_RATE = 0.01;

    private double principalAmount;
    private double interestRate;
    private int tenureMonths;
    private String maturityDate;
    private boolean matured;

    public FixedDepositAccount(String customerId, String branchCode, int pin,
                                double principalAmount, double interestRate, int tenureMonths) {
        super(customerId, branchCode, AccountType.FIXED_DEPOSIT, pin);
        this.principalAmount = principalAmount;
        this.setBalance(principalAmount);
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.maturityDate = DateUtils.addMonths(DateUtils.today(), tenureMonths);
        this.matured = false;
    }

    public FixedDepositAccount() {}

    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public int getTenureMonths() { return tenureMonths; }
    public String getMaturityDate() { return maturityDate; }
    public boolean isMatured() { return matured || DateUtils.isOverdue(maturityDate); }

    public double calculateMaturityValue() {
        double interest = principalAmount * interestRate * tenureMonths / 12;
        return Math.round((principalAmount + interest) * 100.0) / 100.0;
    }

    public double prematureWithdraw() throws BankingException {
        if (isMatured()) {
            return closeMaturedDeposit();
        }
        double penalty = principalAmount * PENALTY_RATE;
        double payout = principalAmount - penalty;
        this.setBalance(0);
        this.setStatus(AccountStatus.CLOSED);
        return Math.round(payout * 100.0) / 100.0;
    }

    public double closeMaturedDeposit() {
        double maturityValue = calculateMaturityValue();
        this.setBalance(0);
        this.setStatus(AccountStatus.CLOSED);
        this.matured = true;
        return maturityValue;
    }

    public void setPrincipalAmount(double principalAmount) { this.principalAmount = principalAmount; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    public void setMaturityDate(String maturityDate) { this.maturityDate = maturityDate; }
    public void setMatured(boolean matured) { this.matured = matured; }
}
