package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class DepositRequest {
    @NotBlank
    private String accountNumber;
    @Positive
    private double amount;

    public String getAccountNumber() { return accountNumber; }
    public double getAmount() { return amount; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setAmount(double amount) { this.amount = amount; }
}
