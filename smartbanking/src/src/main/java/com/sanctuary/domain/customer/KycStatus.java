package com.sanctuary.domain.customer;

public enum KycStatus {
    PENDING("Pending Verification"),
    VERIFIED("KYC Verified"),
    REJECTED("KYC Rejected"),
    EXPIRED("KYC Expired");

    private final String display;

    KycStatus(String display) { this.display = display; }
    public String display() { return display; }

    public static KycStatus fromString(String s) {
        for (KycStatus k : values()) {
            if (k.name().equalsIgnoreCase(s) || k.display.equalsIgnoreCase(s)) return k;
        }
        return PENDING;
    }
}
