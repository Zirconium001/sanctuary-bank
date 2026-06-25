package com.sanctuary.exception;

public class InsufficientBalanceException extends BankingException {
    public InsufficientBalanceException(double available, double requested) {
        super("Insufficient balance. Available: " + available + ", Requested: " + requested, "ERR-404", "HIGH");
    }
}
