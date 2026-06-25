package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class WithdrawRequest {
    @NotBlank
    private String accountNumber;
    private int pin;
    @Positive
    private double amount;

    public String getAccountNumber() { return accountNumber; }
    public int getPin() { return pin; }
    public double getAmount() { return amount; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setPin(int pin) { this.pin = pin; }
    public void setAmount(double amount) { this.amount = amount; }
}
