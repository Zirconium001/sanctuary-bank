package com.sanctuary.domain.account;

import com.sanctuary.exception.*;
import com.sanctuary.util.DateUtils;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("LOAN")
public class LoanAccount extends Account {
    private double principalAmount;
    private double interestRate;
    private int tenureMonths;
    private double monthlyInstallment;
    private double amountPaid;
    private String loanType;
    private String loanId;
    private String disbursementDate;
    private String nextDueDate;
    private int installmentsPaid;

    public LoanAccount(String customerId, String branchCode, int pin,
                        double principalAmount, double interestRate,
                        int tenureMonths, String loanType) {
        super(customerId, branchCode, AccountType.LOAN, pin);
        this.loanId = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.principalAmount = principalAmount;
        this.setBalance(principalAmount);
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.loanType = loanType;
        this.amountPaid = 0;
        this.installmentsPaid = 0;
        this.disbursementDate = DateUtils.today();
        this.monthlyInstallment = calculateEmi();
        this.nextDueDate = DateUtils.addMonths(DateUtils.today(), 1);
    }

    public LoanAccount() {}

    public String getLoanId() { return loanId; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public int getTenureMonths() { return tenureMonths; }
    public double getMonthlyInstallment() { return monthlyInstallment; }
    public double getAmountPaid() { return amountPaid; }
    public double getOutstandingBalance() { return Math.max(0, principalAmount - amountPaid); }
    public String getLoanType() { return loanType; }
    public String getDisbursementDate() { return disbursementDate; }
    public String getNextDueDate() { return nextDueDate; }
    public int getInstallmentsPaid() { return installmentsPaid; }
    public boolean isOverdue() { return DateUtils.isOverdue(nextDueDate); }

    private double calculateEmi() {
        double monthlyRate = interestRate / 12;
        double emi = principalAmount * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)
                   / (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        return Math.round(emi * 100.0) / 100.0;
    }

    public void makeRepayment(double amount) throws BankingException {
        if (amount <= 0) throw new InvalidAmountException("Repayment must be positive.");
        double outstanding = getOutstandingBalance();
        if (amount > outstanding) amount = outstanding;
        amountPaid += amount;
        setBalance(getOutstandingBalance());
        installmentsPaid++;
        nextDueDate = DateUtils.addMonths(nextDueDate, 1);
        touchLastTransaction();
        if (getOutstandingBalance() <= 0) {
            setStatus(AccountStatus.CLOSED);
        }
    }

    public void setPrincipalAmount(double principalAmount) { this.principalAmount = principalAmount; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    public void setMonthlyInstallment(double monthlyInstallment) { this.monthlyInstallment = monthlyInstallment; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public void setDisbursementDate(String disbursementDate) { this.disbursementDate = disbursementDate; }
    public void setNextDueDate(String nextDueDate) { this.nextDueDate = nextDueDate; }
    public void setInstallmentsPaid(int installmentsPaid) { this.installmentsPaid = installmentsPaid; }
}
