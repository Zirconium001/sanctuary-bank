package com.sanctuary.exception;

public class BankingException extends Exception {
    private final String errorCode;
    private final String severity;

    public BankingException(String message) {
        super(message);
        this.errorCode = "ERR-000";
        this.severity = "LOW";
    }

    public BankingException(String message, String errorCode, String severity) {
        super(message);
        this.errorCode = errorCode;
        this.severity = severity;
    }

    public String getErrorCode() { return errorCode; }
    public String getSeverity() { return severity; }
}
