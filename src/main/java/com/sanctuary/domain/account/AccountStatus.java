package com.sanctuary.domain.account;

public enum AccountStatus {
    ACTIVE("Active"),
    FROZEN("Frozen"),
    DORMANT("Dormant"),
    CLOSED("Closed");

    private final String display;

    AccountStatus(String display) { this.display = display; }
    public String display() { return display; }

    public static AccountStatus fromString(String s) {
        for (AccountStatus as : values()) {
            if (as.name().equalsIgnoreCase(s) || as.display.equalsIgnoreCase(s)) return as;
        }
        return ACTIVE;
    }
}
