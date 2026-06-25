package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class LoanApplicationRequest {
    @NotBlank
    private String customerId;
    private String branchCode = "BR-001";
    @NotBlank
    private String loanType;
    @Positive
    private double principal;
    @Min(1) @Max(240)
    private int tenureMonths;
    private int pin;

    public String getCustomerId() { return customerId; }
    public String getBranchCode() { return branchCode; }
    public String getLoanType() { return loanType; }
    public double getPrincipal() { return principal; }
    public int getTenureMonths() { return tenureMonths; }
    public int getPin() { return pin; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public void setPrincipal(double principal) { this.principal = principal; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    public void setPin(int pin) { this.pin = pin; }
}
