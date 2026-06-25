package com.sanctuary.exception;

public class PinMismatchException extends BankingException {
    public PinMismatchException() {
        super("Incorrect PIN.", "ERR-405", "HIGH");
    }
}
