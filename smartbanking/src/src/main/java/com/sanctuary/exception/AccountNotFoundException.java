package com.sanctuary.exception;

public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber, "ERR-401", "HIGH");
    }
}
