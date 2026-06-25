package com.sanctuary.exception;

public class DailyLimitExceededException extends BankingException {
    public DailyLimitExceededException(String operation, double limit) {
        super("Daily " + operation + " limit exceeded. Limit: " + limit, "ERR-407", "HIGH");
    }
}
