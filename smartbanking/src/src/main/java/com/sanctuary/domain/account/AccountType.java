package com.sanctuary.domain.account;

public enum AccountType {
    SAVINGS("Savings Account"),
    CHECKING("Checking Account"),
    FIXED_DEPOSIT("Fixed Deposit"),
    LOAN("Loan Account");

    private final String display;

    AccountType(String display) { this.display = display; }
    public String display() { return display; }

    public static AccountType fromString(String s) {
        for (AccountType t : values()) {
            if (t.name().equalsIgnoreCase(s) || t.display.equalsIgnoreCase(s)) return t;
        }
        return SAVINGS;
    }
}
