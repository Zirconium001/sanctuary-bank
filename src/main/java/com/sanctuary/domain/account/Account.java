package com.sanctuary.domain.account;

import com.sanctuary.util.DateUtils;
import com.sanctuary.util.IdGenerator;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type_disc", discriminatorType = DiscriminatorType.STRING)
@Table(name = "accounts")
public abstract class Account {

    @Id
    private String accountNumber;

    private String customerId;
    private String branchCode;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String dateOpened;
    private String lastTransactionDate;
    private int pin;
    private int failedPinAttempts;
    private boolean pinLocked;
    private String accountName;

    protected Account(String customerId, String branchCode, AccountType accountType, int pin) {
        this.accountNumber = IdGenerator.nextAccountNumber();
        this.customerId = customerId;
        this.branchCode = branchCode;
        this.accountType = accountType;
        this.balance = 0.0;
        this.status = AccountStatus.ACTIVE;
        this.dateOpened = DateUtils.today();
        this.lastTransactionDate = DateUtils.today();
        this.pin = pin;
        this.failedPinAttempts = 0;
        this.pinLocked = false;
    }

    protected Account() {}

    public String getAccountNumber() { return accountNumber; }
    public String getCustomerId() { return customerId; }
    public String getBranchCode() { return branchCode; }
    public AccountType getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public String getDateOpened() { return dateOpened; }
    public String getLastTransactionDate() { return lastTransactionDate; }
    public boolean isPinLocked() { return pinLocked; }

    public void setStatus(AccountStatus status) { this.status = status; }
    public void setPin(int pin) { this.pin = pin; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean verifyPin(int enteredPin) {
        if (pinLocked) return false;
        if (this.pin == enteredPin) {
            failedPinAttempts = 0;
            return true;
        }
        failedPinAttempts++;
        if (failedPinAttempts >= 3) {
            pinLocked = true;
            status = AccountStatus.FROZEN;
        }
        return false;
    }

    public void resetPinLock() {
        this.pinLocked = false;
        this.failedPinAttempts = 0;
    }

    public void touchLastTransaction() {
        this.lastTransactionDate = DateUtils.today();
        if (status == AccountStatus.DORMANT) {
            status = AccountStatus.ACTIVE;
        }
    }

    public String toFileString() {
        return String.join("|",
            accountNumber, customerId, branchCode, accountType.name(),
            String.valueOf(balance), status.name(), dateOpened, lastTransactionDate,
            String.valueOf(pin), String.valueOf(failedPinAttempts), String.valueOf(pinLocked)
        );
    }

    protected void parseBaseFields(String line) {
        String[] p = line.split("\\|", -1);
        accountNumber = p[0];
        customerId = p[1];
        branchCode = p[2];
        accountType = AccountType.fromString(p[3]);
        balance = Double.parseDouble(p[4]);
        status = AccountStatus.fromString(p[5]);
        dateOpened = p[6];
        lastTransactionDate = p[7];
        pin = Integer.parseInt(p[8]);
        failedPinAttempts = Integer.parseInt(p[9]);
        pinLocked = Boolean.parseBoolean(p[10]);
    }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public void setDateOpened(String dateOpened) { this.dateOpened = dateOpened; }
    public void setLastTransactionDate(String lastTransactionDate) { this.lastTransactionDate = lastTransactionDate; }
    public void setFailedPinAttempts(int failedPinAttempts) { this.failedPinAttempts = failedPinAttempts; }
    public void setPinLocked(boolean pinLocked) { this.pinLocked = pinLocked; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
}
