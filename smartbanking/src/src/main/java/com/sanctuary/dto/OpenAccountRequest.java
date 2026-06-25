package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class OpenAccountRequest {
    @NotBlank
    private String customerId;
    private String branchCode = "BR-001";
    private int pin;
    @Positive
    private double initialDeposit;
    private String accountName;

    public String getCustomerId() { return customerId; }
    public String getBranchCode() { return branchCode; }
    public int getPin() { return pin; }
    public double getInitialDeposit() { return initialDeposit; }
    public String getAccountName() { return accountName; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public void setPin(int pin) { this.pin = pin; }
    public void setInitialDeposit(double initialDeposit) { this.initialDeposit = initialDeposit; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
}
