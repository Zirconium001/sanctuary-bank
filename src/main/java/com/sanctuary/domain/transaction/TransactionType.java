package com.sanctuary.domain.transaction;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER_OUT("Transfer Out"),
    TRANSFER_IN("Transfer In"),
    INTEREST_CREDIT("Interest Credited"),
    FEE_DEDUCTION("Fee Deducted"),
    LOAN_DISBURSEMENT("Loan Disbursement"),
    LOAN_REPAYMENT("Loan Repayment"),
    FD_CREATION("Fixed Deposit Created"),
    FD_MATURITY("Fixed Deposit Matured"),
    FD_PREMATURE_CLOSURE("FD Premature Closure"),
    PIN_CHANGE("PIN Changed"),
    ACCOUNT_CLOSURE("Account Closed"),
    ACCOUNT_REOPENED("Account Reopened"),
    SYSTEM_ADJUSTMENT("System Adjustment");

    private final String display;

    TransactionType(String display) { this.display = display; }
    public String display() { return display; }

    public static TransactionType fromString(String s) {
        for (TransactionType t : values()) {
            if (t.name().equalsIgnoreCase(s) || t.display.equalsIgnoreCase(s)) return t;
        }
        return SYSTEM_ADJUSTMENT;
    }
}
