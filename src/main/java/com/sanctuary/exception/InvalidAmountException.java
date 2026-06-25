package com.sanctuary.exception;

public class InvalidAmountException extends BankingException {
    public InvalidAmountException(String detail) {
        super("Invalid amount: " + detail, "ERR-403", "MEDIUM");
    }
}
