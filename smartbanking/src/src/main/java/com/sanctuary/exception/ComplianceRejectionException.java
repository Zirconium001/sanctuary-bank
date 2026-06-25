package com.sanctuary.exception;

public class ComplianceRejectionException extends BankingException {
    public ComplianceRejectionException(String reason) {
        super("Compliance rejection: " + reason, "ERR-408", "HIGH");
    }
}
