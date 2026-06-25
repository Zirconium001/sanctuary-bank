package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class TransferRequest {
    @NotBlank
    private String fromAccount;
    private int pin;
    @NotBlank
    private String toAccount;
    @Positive
    private double amount;

    public String getFromAccount() { return fromAccount; }
    public int getPin() { return pin; }
    public String getToAccount() { return toAccount; }
    public double getAmount() { return amount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public void setPin(int pin) { this.pin = pin; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public void setAmount(double amount) { this.amount = amount; }
}
