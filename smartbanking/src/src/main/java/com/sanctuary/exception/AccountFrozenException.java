package com.sanctuary.exception;

public class AccountFrozenException extends BankingException {
    public AccountFrozenException(String accountNumber) {
        super("Account " + accountNumber + " is frozen due to multiple failed PIN attempts.", "ERR-406", "HIGH");
    }
}
