package com.sanctuary.exception;

public class CustomerNotFoundException extends BankingException {
    public CustomerNotFoundException(String customerId) {
        super("Customer not found: " + customerId, "ERR-402", "HIGH");
    }
}
