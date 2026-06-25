package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class FdCreateRequest {
    @NotBlank
    private String customerId;
    private String branchCode = "BR-001";
    @Positive
    private double amount;
    @Min(1) @Max(240)
    private int tenureMonths;
    private int pin;

    public String getCustomerId() { return customerId; }
    public String getBranchCode() { return branchCode; }
    public double getAmount() { return amount; }
    public int getTenureMonths() { return tenureMonths; }
    public int getPin() { return pin; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    public void setPin(int pin) { this.pin = pin; }
}
