package com.sanctuary.domain.transaction;

import com.sanctuary.util.DateUtils;
import com.sanctuary.util.IdGenerator;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class TransactionRecord {

    @Id
    private String transactionId;
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private double amount;
    private double balanceBefore;
    private double balanceAfter;
    private String timestamp;
    private String description;
    private String referenceNumber;
    private String channel;

    public TransactionRecord(String accountNumber, TransactionType type,
                              double amount, double balanceBefore, String description) {
        this.transactionId = IdGenerator.nextTransactionId();
        this.accountNumber = accountNumber;
        this.transactionType = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceBefore + amount;
        this.timestamp = DateUtils.now();
        this.description = description;
        this.referenceNumber = "REF-" + transactionId;
        this.channel = "MOBILE";
    }

    public TransactionRecord() {}

    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public double getBalanceBefore() { return balanceBefore; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public String getReferenceNumber() { return referenceNumber; }
    public String getChannel() { return channel; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setBalanceBefore(double balanceBefore) { this.balanceBefore = balanceBefore; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setDescription(String description) { this.description = description; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public void setChannel(String channel) { this.channel = channel; }
}
