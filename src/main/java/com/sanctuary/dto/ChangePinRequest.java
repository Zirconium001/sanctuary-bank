package com.sanctuary.dto;

import jakarta.validation.constraints.*;

public class ChangePinRequest {
    @NotBlank
    private String accountNumber;
    private int oldPin;
    private int newPin;

    public String getAccountNumber() { return accountNumber; }
    public int getOldPin() { return oldPin; }
    public int getNewPin() { return newPin; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setOldPin(int oldPin) { this.oldPin = oldPin; }
    public void setNewPin(int newPin) { this.newPin = newPin; }
}
